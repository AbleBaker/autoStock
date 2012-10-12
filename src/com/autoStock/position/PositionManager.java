/**
 * 
 */
package com.autoStock.position;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.order.OrderDefinitions.OrderMode;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.tools.Lock;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class PositionManager implements PositionStatusListener {
	private static PositionManager instance = new PositionManager();
	private volatile PositionGenerator positionGenerator = new PositionGenerator();
	private volatile PositionExecutor positionExecutor = new PositionExecutor();
	private volatile ArrayList<Position> listOfPosition = new ArrayList<Position>();
	public OrderMode orderMode = OrderMode.mode_simulated;
	private Lock lock = new Lock();
	
	public static PositionManager getInstance(){
		return instance;
	}

	public synchronized Position executePosition(QuoteSlice quoteSlice, Exchange exchange, Signal signal, PositionType positionType) {
		synchronized (lock) {
			if (positionType == PositionType.position_long_entry) {
				Position position = positionGenerator.generatePosition(quoteSlice, signal, positionType, exchange);
				if (position != null){
					position.setPositionListener(this);
					listOfPosition.add(position);
					positionExecutor.executeLongEntry(position);
				}
				return position;
			} else if (positionType == PositionType.position_short_entry) {
				Position position = positionGenerator.generatePosition(quoteSlice, signal, positionType, exchange);
				if (position != null){
					position.setPositionListener(this);
					listOfPosition.add(position);
					positionExecutor.executeShortEntry(position);
				}
				return position;
			} else if (positionType == PositionType.position_long_exit) {
				Position position = getPosition(quoteSlice.symbol);
				positionExecutor.executeLongExit(position);
				return position;
			} else if (positionType == PositionType.position_short_exit) {
				Position position = getPosition(quoteSlice.symbol);
				positionExecutor.executeShortExit(position);	
				return position;
			} else {
				throw new UnsupportedOperationException();
			}
		}
	}

	public void updatePositionPrice(QuoteSlice quoteSlice, Position position) {
		if (position != null) {
			position.updatePositionUnitPrice(quoteSlice.priceClose);
		}
	}

	public void executeExitAll() {
		synchronized (lock) {
			if (listOfPosition.size() == 0) {
				Co.println("--> No positions to sell");
			}else{
				Co.println("--> Exiting all positions");
			}

			for (Position position : listOfPosition) {
				if (position.positionType == PositionType.position_long) {
					positionExecutor.executeLongExit(position);
				} else if (position.positionType == PositionType.position_short) {
					positionExecutor.executeShortExit(position);
				} else {
					throw new IllegalStateException("No condition matched PositionType: " + position.positionType.name());
				}
			}
			listOfPosition.clear();
		}
	}

	public synchronized Position getPosition(Symbol symbol) {
		synchronized (lock) {
			for (Position position : listOfPosition) {
				if (position.symbol.symbolName.equals(symbol.symbolName)) {
					return position;
				}
			}
		}

		return null;
	}

	public synchronized double getCurrentProfitLossAfterComission() {
		synchronized (lock) {
			double currentProfitLoss = 0;
			for (Position position : listOfPosition) {
				currentProfitLoss += position.getPositionProfitLossAfterComission();
			}
			
			return MathTools.round(currentProfitLoss);
		}
	}
	
	public synchronized double getCurrentProfitLossBeforeComission() {
		synchronized (lock) {
			double currentProfitLoss = 0;
			for (Position position : listOfPosition) {
				currentProfitLoss += position.getPositionProfitLossBeforeComission();
			}
			
			return MathTools.round(currentProfitLoss);
		}
	}

	public synchronized int getPositionListSize() {
		return listOfPosition.size();
	}

	public double getAllPositionValueIncludingFees() {
		synchronized(lock){
			double valueOfAllPositions = 0; 
			for (Position position : listOfPosition){
				valueOfAllPositions += position.getPositionValue().valueCurrentWithFees;
			}
			
			return valueOfAllPositions;
		}
	}

	@Override
	public synchronized void positionStatusChange(Position position) {
		synchronized(lock){
//			Co.println("--> PositionManager, position status change: " + position.positionType.name());
			if (position.positionType == PositionType.position_exited || position.positionType == PositionType.position_cancelled){
				listOfPosition.remove(position);
				position = null; //?
//				Co.println("--> Removed... " + listOfPosition.size());
			}
		}
	}
}
