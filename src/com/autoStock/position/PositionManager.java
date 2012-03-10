/**
 * 
 */
package com.autoStock.position;

import java.util.ArrayList;
import java.util.Iterator;

import com.autoStock.balance.AccountBalance;
import com.autoStock.signal.Signal;
import com.autoStock.trading.types.TypePosition;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionManager {
	private ArrayList<TypePosition> listOfPosition = new ArrayList<TypePosition>();
	private AccountBalance accountBalance = AccountBalance.instance;
	private PositionGenerator positionGenerator = new PositionGenerator();
	
	private void generateNewPosition(TypePosition typePosition, Signal signal){
		positionGenerator.generatePosition(typePosition, signal);
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
