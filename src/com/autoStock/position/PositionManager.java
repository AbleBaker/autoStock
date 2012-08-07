/**
 * 
 */
package com.autoStock.position;

import java.util.ArrayList;

import com.autoStock.finance.Account;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.tools.Lock;
import com.autoStock.trading.types.Position;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionManager {
	public static PositionManager instance = new PositionManager();
	private Account account = Account.instance;
	private PositionGenerator positionGenerator = new PositionGenerator(account);
	private ArrayList<Position> listOfPosition = new ArrayList<Position>();
	private Lock lock = new Lock();
	
	public synchronized Position suggestPosition(QuoteSlice quoteSlice, Signal signal, PositionType positionType){
		synchronized (lock) {
			if (positionType == PositionType.position_long_entry){
				return executeLongEntry(quoteSlice, signal, positionType);
			}
			else if (positionType == PositionType.position_short_entry){
				return executeShortEntry(quoteSlice, signal, positionType);
			}
			else if (positionType == PositionType.position_long_exit){
				Position typePosition = executeLongExit(getPosition(quoteSlice.symbol)).clone();
				listOfPosition.remove(getPosition(quoteSlice.symbol));
				return typePosition;
				
			}
			else if (positionType == PositionType.position_short_exit){
				Position typePosition = executeShortExit(getPosition(quoteSlice.symbol), positionType).clone();
				listOfPosition.remove(getPosition(quoteSlice.symbol));
				return typePosition;
			}
			else {
				throw new UnsupportedOperationException();
			}
		}
	}
	
	private Position executeLongEntry(QuoteSlice quoteSlice, Signal signal, PositionType positionType){
		Position typePosition = positionGenerator.generatePosition(quoteSlice, signal, positionType);
		account.changeBankBalance(-1 * (typePosition.units * typePosition.price), account.getTransactionCost(typePosition.units, typePosition.price));
		listOfPosition.add(typePosition);
		PositionCallback.setPositionSuccess(typePosition);

		return typePosition;
	}
	
	private Position executeShortEntry(QuoteSlice quoteSlice, Signal signal, PositionType positionType){
		Position typePosition = positionGenerator.generatePosition(quoteSlice, signal, positionType);
		listOfPosition.add(typePosition);
		
		account.changeBankBalance(-1 * (typePosition.units * typePosition.price), account.getTransactionCost(typePosition.units, typePosition.price));
		PositionCallback.setPositionSuccess(typePosition);
		
		return typePosition;
	}
	
	private Position executeLongExit(Position position){
		position.positionType = PositionType.position_long_exit;
		
		account.changeBankBalance(position.units * position.lastKnownPrice, account.getTransactionCost(position.units, position.price));
		PositionCallback.setPositionSuccess(position);
		
		return position;
	}
	
	private Position executeShortExit(Position position, PositionType positionType){
		position.positionType = PositionType.position_short_exit;

		account.changeBankBalance(position.units * position.price, account.getTransactionCost(position.units, position.price));
		PositionCallback.setPositionSuccess(position);
		
		return position;
	}		
	
	public synchronized void updatePositionPrice(QuoteSlice quoteSlice, Position typePosition){
		synchronized (lock) {
			if (typePosition != null){
				typePosition.lastKnownPrice = quoteSlice.priceClose;
			}
		}
	}
	
	public void executeSellAll(){
		synchronized(lock){
//			Co.println("Exiting all positions");
//			if (listOfPosition.size() == 0){Co.println("Not holding any positions...");}
			
			for (Position typePosition : listOfPosition){
				if (typePosition.positionType == PositionType.position_long){
					executeLongExit(typePosition);
				}else if (typePosition.positionType == PositionType.position_short){
					executeShortExit(typePosition, typePosition.positionType);
				}else {
					throw new IllegalStateException("No condition matched PositionType: " + typePosition.positionType.name());
				}
			}
			
			listOfPosition.clear();
		}
	}
	
	public synchronized Position getPosition(String symbol){
		synchronized(lock){
			for (Position typePosition : listOfPosition){
				if (typePosition.symbol.equals(symbol)){
					return typePosition;
				}
			}
		}
		
		return null;
	}
}
