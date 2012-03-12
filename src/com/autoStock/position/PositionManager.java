/**
 * 
 */
package com.autoStock.position;

import java.util.ArrayList;
import java.util.Iterator;

import com.autoStock.balance.AccountBalance;
import com.autoStock.signal.Signal;
import com.autoStock.trading.types.TypePosition;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionManager {
	private AccountBalance accountBalance = AccountBalance.instance;
	private PositionGenerator positionGenerator = new PositionGenerator();
	private ArrayList<TypePosition> listOfPosition = new ArrayList<TypePosition>();
	
	private void generateNewPosition(TypeQuoteSlice typeQuoteSlice, Signal signal){
		positionGenerator.generatePosition(typeQuoteSlice, signal);
	}
	
	private void addPosition(TypePosition typePosition){
		synchronized(listOfPosition){
			listOfPosition.add(typePosition);
		}
	}
	
	private void removePosition(TypePosition typePosition){
		synchronized(listOfPosition){
			listOfPosition.remove(typePosition);
		}
	}
}
