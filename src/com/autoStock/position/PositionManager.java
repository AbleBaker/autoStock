/**
 * 
 */
package com.autoStock.position;

import java.util.ArrayList;

import com.autoStock.Co;
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
	
	public Position suggestPosition(QuoteSlice typeQuoteSlice, Signal signal, PositionType positionType){		
		if (positionType == PositionType.position_long_entry){
			return executeLongEntry(typeQuoteSlice, signal, positionType);
		}
		else if (positionType == PositionType.position_short_entry){
			return executeShortEntry(typeQuoteSlice, signal, positionType);
		}
		else if (positionType == PositionType.position_long_exit){
			Position typePosition = executeLongExit(getPosition(typeQuoteSlice.symbol), positionType).clone();
			listOfPosition.remove(getPosition(typeQuoteSlice.symbol));
			return typePosition;
			
		}
		else if (positionType == PositionType.position_short_exit){
			Position typePosition = executeShortExit(getPosition(typeQuoteSlice.symbol), positionType);
			listOfPosition.remove(getPosition(typeQuoteSlice.symbol));
			return typePosition;
		}
		else {
			throw new UnsupportedOperationException();
		}		
	}
	
	private Position executeLongEntry(QuoteSlice typeQuoteSlice, Signal signal, PositionType positionType){
		synchronized (lock) {
			Position typePosition = positionGenerator.generatePosition(typeQuoteSlice, signal, positionType);
			account.changeBankBalance(-1 * (typePosition.units * typePosition.price), account.getTransactionCost(typePosition.units, typePosition.price));
			listOfPosition.add(typePosition);
			PositionCallback.setPositionSuccess(typePosition);
			
			//Co.println("\n-------> Long position entry (symbol, units, price, total, cost): " + typePosition.symbol + ", " + typePosition.units + ", " + typePosition.price + ", " + (typePosition.price * typePosition.units) + ", " + account.getTransactionCost(typePosition.units, typePosition.price));
			//Co.println("Account balance: " + Account.instance.getBankBalance() + " Fees paid: " + Account.instance.getTransactionFeesPaid());
			return typePosition;	
		}
	}
	
	private Position executeShortEntry(QuoteSlice typeQuoteSlice, Signal signal, PositionType positionType){
		synchronized (lock){
			Position typePosition = positionGenerator.generatePosition(typeQuoteSlice, signal, positionType);
			listOfPosition.add(typePosition);
			account.changeBankBalance(-1 * (typePosition.units * typePosition.price), account.getTransactionCost(typePosition.units, typePosition.price));
			PositionCallback.setPositionSuccess(typePosition);
			
			//Co.println("\n-------> Short position entry (symbol, units, price, total, cost): " + typePosition.symbol + ", " + typePosition.units + ", " + typePosition.price + ", " + (typePosition.price * typePosition.units) + ", " + account.getTransactionCost(typePosition.units, typePosition.price));
			//Co.println("Account balance: " + Account.instance.getBankBalance() + " Fees paid: " + Account.instance.getTransactionFeesPaid());
			return typePosition;
		}
	}
	
	private Position executeLongExit(Position typePosition, PositionType positionType){
		synchronized (lock){
			typePosition.positionType = PositionType.position_long_exit;
			
			account.changeBankBalance(typePosition.units * typePosition.lastKnownPrice, account.getTransactionCost(typePosition.units, typePosition.price));
			PositionCallback.setPositionSuccess(typePosition);
			
			//Co.println("\n-------> Long position exit (symbol, units, price, total, cost): " + typePosition.symbol + ", " + typePosition.units + ", " + typePosition.lastKnownPrice + ", " + (typePosition.lastKnownPrice * typePosition.units) + ", " + account.getTransactionCost(typePosition.units, typePosition.price));
			//Co.println("Account balance: " + Account.instance.getBankBalance() + " Fees paid: " + Account.instance.getTransactionFeesPaid());
			
			return typePosition;
		}
	}
	
	private Position executeShortExit(Position typePosition, PositionType positionType){
		synchronized(lock){
			typePosition.positionType = PositionType.position_short_exit;

			account.changeBankBalance(typePosition.units * typePosition.price, account.getTransactionCost(typePosition.units, typePosition.price));
			PositionCallback.setPositionSuccess(typePosition);
			
			//Co.println("\n-------> Short position exit (symbol, units, price, total, cost): " + typePosition.symbol + ", " + typePosition.units + ", " + typePosition.price + ", " + (typePosition.price * typePosition.units) + ", " + account.getTransactionCost(typePosition.units, typePosition.price));
			//Co.println("Account balance: " + Account.instance.getBankBalance() + " Fees paid: " + Account.instance.getTransactionFeesPaid());
			
			return typePosition;
		}
	}		
	
	public void updatePositionPrice(QuoteSlice typeQuoteSlice, Position typePosition){
		if (typePosition != null){
			typePosition.lastKnownPrice = typeQuoteSlice.priceClose;
		}
	}
	
	public void executeSellAll(){
		Co.println("Induced sell all");
		if (listOfPosition.size() == 0){
			Co.println("Not holding any positions...");
		}
		synchronized(lock){
			for (Position typePosition : listOfPosition){
				if (typePosition.positionType == PositionType.position_long){
					executeLongExit(typePosition, typePosition.positionType);
				}else if (typePosition.positionType == PositionType.position_short){
					executeShortExit(typePosition, typePosition.positionType);
				}else {
					throw new IllegalStateException("No condition matched PositionType: " + typePosition.positionType.name());
				}
			}
			
			listOfPosition.clear();
		}
	}
	
	public Position getPosition(String symbol){
		synchronized(listOfPosition){
			for (Position typePosition : listOfPosition){
				if (typePosition.symbol.equals(symbol)){
					return typePosition;
				}
			}
		}
		
		return null;
	}
}
