/**
 * 
 */
package com.autoStock.position;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.TypePosition;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionManager {
	public static PositionManager instance = new PositionManager();
	private Account account = Account.instance;
	private PositionGenerator positionGenerator = new PositionGenerator(account);
	private ArrayList<TypePosition> listOfPosition = new ArrayList<TypePosition>();
	private final int MAX_POSITIONS = 10;
	
	public void suggestPosition(TypeQuoteSlice typeQuoteSlice, Signal signal, PositionType positionType){
		
		updatePositionPrice(typeQuoteSlice);
		
		if (positionType == PositionType.position_buy){
			executeBuy(typeQuoteSlice, signal, positionType);
		}
		else if (positionType == PositionType.position_sell){
			executeSell(typeQuoteSlice, signal, positionType);
		}
		else if (positionType == PositionType.position_short){
			executeShort(typeQuoteSlice, signal, positionType);
		}else {
			throw new UnsupportedOperationException();
		}
	}
	
	public void updatePositionPrice(TypeQuoteSlice typeQuoteSlice){
		if (getPosition(typeQuoteSlice.symbol) != null){
			getPosition(typeQuoteSlice.symbol).lastKnownPrice = typeQuoteSlice.priceClose;
		}
	}
	
	private void executeBuy(TypeQuoteSlice typeQuoteSlice, Signal signal, PositionType positionType){
		Co.println("Induced buy @ " + DateTools.getPrettyDate(typeQuoteSlice.dateTime));
		TypePosition typePosition = positionGenerator.generatePosition(typeQuoteSlice, signal, positionType);
		positionBuy(typePosition);
	}
	
	private void executeSell(TypeQuoteSlice typeQuoteSlice, Signal signal, PositionType positionType){
		Co.println("Induced sell @ " + DateTools.getPrettyDate(typeQuoteSlice.dateTime));
		positionSell(getPosition(typeQuoteSlice.symbol), true);
	}
	
	private void executeShort(TypeQuoteSlice typeQuoteSlice, Signal signal, PositionType positionType){
		Co.println("Induced short @ " + DateTools.getPrettyDate(typeQuoteSlice.dateTime));
		TypePosition typePosition = positionGenerator.generatePosition(typeQuoteSlice, signal, positionType);
		positionShort(typePosition);
	}
	
	public void executeSellAll(){
		Co.println("Induced sell all");
		if (listOfPosition.size() == 0){
			Co.println("Not holding any positions...");
		}
		synchronized(listOfPosition){
			for (TypePosition typePosition : listOfPosition){
				if (typePosition.positionType == PositionType.position_buy){
					positionSell(typePosition, false);
				}else if (typePosition.positionType == PositionType.position_short){
					positionSell(typePosition, false);
				}else {
					throw new IllegalStateException();
				}
			}
			
			listOfPosition.clear();
		}
	}
	
	private void positionBuy(TypePosition typePosition){
		synchronized(listOfPosition){
			account.changeBankBalance(-1 * (typePosition.units * typePosition.price), account.getTransactionCost(typePosition.units, typePosition.price));
			listOfPosition.add(typePosition);
			Co.println("-------> Buy position (symbol, units, price, total, cost): " + typePosition.symbol + ", " + typePosition.units + ", " + typePosition.price + ", " + (typePosition.price * typePosition.units) + ", " + account.getTransactionCost(typePosition.units, typePosition.price));
		}
	}
	
	private void positionSell(TypePosition typePosition, boolean removeFromList){
		synchronized(listOfPosition){
			if (typePosition.positionType == PositionType.position_buy){
				account.changeBankBalance(typePosition.units * typePosition.lastKnownPrice, account.getTransactionCost(typePosition.units, typePosition.price));
			}else if (typePosition.positionType == PositionType.position_short){
				account.changeBankBalance(typePosition.units * typePosition.lastKnownPrice, account.getTransactionCost(typePosition.units, typePosition.price));
			}else {
				throw new IllegalStateException();
			}
			
			if (removeFromList){listOfPosition.remove(typePosition);}
			Co.println("-------> Sell position (symbol, units, price, total, cost): " + typePosition.symbol + ", " + typePosition.units + ", " + typePosition.lastKnownPrice + ", " + (typePosition.lastKnownPrice * typePosition.units) + ", " + account.getTransactionCost(typePosition.units, typePosition.price));
		}
	}
	
	private void positionShort(TypePosition typePosition){
		synchronized (listOfPosition){
			listOfPosition.add(typePosition);
			account.changeBankBalance(-1 * (typePosition.units * typePosition.price), account.getTransactionCost(typePosition.units, typePosition.price));
			Co.println("-------> Short position (symbol, units, price, total, cost): " + typePosition.symbol + ", " + typePosition.units + ", " + typePosition.price + ", " + (typePosition.price * typePosition.units) + ", " + account.getTransactionCost(typePosition.units, typePosition.price));
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
}
