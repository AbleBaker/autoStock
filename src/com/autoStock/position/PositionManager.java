/**
 * 
 */
package com.autoStock.position;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.finance.Currency;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.tools.Lock;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * 
 */
public class PositionManager {
	public static PositionManager instance = new PositionManager();
	private volatile Account account = Account.instance;
	private volatile PositionGenerator positionGenerator = new PositionGenerator(account);
	private volatile ArrayList<Position> listOfPosition = new ArrayList<Position>();
	private Lock lock = new Lock();

	public synchronized Position executePosition(QuoteSlice quoteSlice, Signal signal, PositionType positionType) {
		synchronized (lock) {
			if (positionType == PositionType.position_long_entry) {
				return executeLongEntry(quoteSlice, signal, positionType);
			} else if (positionType == PositionType.position_short_entry) {
				return executeShortEntry(quoteSlice, signal, positionType);
			} else if (positionType == PositionType.position_long_exit) {
				Position position = executeLongExit(getPosition(quoteSlice.symbol)).clone();
				listOfPosition.remove(getPosition(quoteSlice.symbol));
				return position;

			} else if (positionType == PositionType.position_short_exit) {
				Position position = executeShortExit(getPosition(quoteSlice.symbol), positionType).clone();
				listOfPosition.remove(getPosition(quoteSlice.symbol));
				return position;
			} else {
				throw new UnsupportedOperationException();
			}
		}
	}

	private Position executeLongEntry(QuoteSlice quoteSlice, Signal signal, PositionType positionType) {
		Position position = positionGenerator.generatePosition(quoteSlice, signal, positionType);
		if (position.units > 0) {
			account.changeBankBalance(-1 * (position.units * position.price), account.getTransactionCost(position.units, position.price));
			listOfPosition.add(position);
			PositionCallback.setPositionSuccess(position);
			return position;
		}
		return null;
	}

	private Position executeShortEntry(QuoteSlice quoteSlice, Signal signal, PositionType positionType) {
		Position position = positionGenerator.generatePosition(quoteSlice, signal, positionType);
		if (position.units > 0) {
			account.changeBankBalance(-1 * (position.units * position.price), account.getTransactionCost(position.units, position.price));
			listOfPosition.add(position);
			PositionCallback.setPositionSuccess(position);
			return position;
		}
		return null;
	}

	private Position executeLongExit(Position position) {
		position.positionType = PositionType.position_long_exit;

		account.changeBankBalance(position.units * position.lastKnownPrice, account.getTransactionCost(position.units, position.price));
		PositionCallback.setPositionSuccess(position);

		return position;
	}

	private Position executeShortExit(Position position, PositionType positionType) {
		position.positionType = PositionType.position_short_exit;

		account.changeBankBalance(position.units * position.price, account.getTransactionCost(position.units, position.price));
		PositionCallback.setPositionSuccess(position);

		return position;
	}

	public synchronized void updatePositionPrice(QuoteSlice quoteSlice, Position position) {
		synchronized (lock) {
			if (position != null) {
				position.lastKnownPrice = quoteSlice.priceClose;
			}
		}
	}

	public void executeSellAll() {
		synchronized (lock) {

			if (listOfPosition.size() == 0) {
				Co.println("--> No positions to sell");
			}

			for (Position typePosition : listOfPosition) {
				if (typePosition.positionType == PositionType.position_long) {
					executeLongExit(typePosition);
				} else if (typePosition.positionType == PositionType.position_short) {
					executeShortExit(typePosition, typePosition.positionType);
				} else {
					throw new IllegalStateException("No condition matched PositionType: " + typePosition.positionType.name());
				}
			}

			listOfPosition.clear();
		}
	}

	public synchronized Position getPosition(String symbol) {
		synchronized (lock) {
			for (Position position : listOfPosition) {
				if (position.symbol.equals(symbol)) {
					return position;
				}
			}
		}

		return null;
	}

	public synchronized double getCurrentProfitLossIncludingFees() {
		synchronized (lock) {
			double currentProfitLoss = 0;
			for (Position position : listOfPosition) {
				currentProfitLoss += position.getPositionProfitLossAfterComission();
			}
			
			return MathTools.round(currentProfitLoss);
		}
	}

	public synchronized int getPositionListSize() {
		return listOfPosition.size();
	}
}
