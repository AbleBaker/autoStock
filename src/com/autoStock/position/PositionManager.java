/**
 * 
 */
package com.autoStock.position;

import java.util.ArrayList;
import java.util.Iterator;

import com.autoStock.Co;
import com.autoStock.balance.AccountBalance;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.trading.types.TypePosition;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionManager {
	public static PositionManager instance = new PositionManager();
	private AccountBalance accountBalance = AccountBalance.instance;
	private PositionGenerator positionGenerator = new PositionGenerator(accountBalance);
	private ArrayList<TypePosition> listOfPosition = new ArrayList<TypePosition>();
	private int maxPositions = 10;
	
	public void suggestPosition(TypeQuoteSlice typeQuoteSlice, Signal signal){
		Co.println("Suggested position: " + signal.currentSignalType.name() + typeQuoteSlice.symbol + " @ " + typeQuoteSlice.priceClose + " signal " + signal.getCombinedSignal());
		TypePosition typePosition = positionGenerator.generatePosition(typeQuoteSlice, signal);
		
		if (getPosition(typePosition.symbol) == null && signal.currentSignalType == SignalType.type_buy){
			addPosition(typePosition);
		}else if (getPosition(typePosition.symbol) != null && signal.currentSignalType == SignalType.type_sell){
			removePosition(typePosition);
		}else{
			//Pass
		}
	}
	
	public TypePosition getPosition(String symbol){
		synchronized(listOfPosition){
			for (TypePosition typePosition : listOfPosition){
				if (typePosition.symbol.equals(symbol)){
					return typePosition;
				}
			}
		}
		
		return null;
	}
	
	private void addPosition(TypePosition typePosition){
		synchronized(listOfPosition){
			accountBalance.changeBankBalance(-1 * (typePosition.units * typePosition.averagePrice));
			listOfPosition.add(typePosition);
			Co.println("Added position: " + typePosition.symbol + "," + typePosition.units + "," + typePosition.averagePrice);
		}
	}
	
	private void removePosition(TypePosition typePosition){
		synchronized(listOfPosition){
			listOfPosition.remove(typePosition);
		}
	}
}
