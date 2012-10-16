/**
 * 
 */
package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGenerator {
	private Account account = Account.getInstance();
	
	public Position generatePosition(QuoteSlice quoteSlice, Signal signal, PositionType positionType, Exchange exchange){
		int positionUnits = (int) getPositionInitialUnits(quoteSlice.priceClose, signal);
		
		if (positionUnits != 0){
			return new Position(positionType, positionUnits, quoteSlice.symbol, exchange, "STK", quoteSlice.priceClose);
		}
		
		return null;
	}
	
	private double getPositionInitialUnits(double price, Signal signal){
		double accountBalance = account.getAccountBalance();
		double units = 0;

		if (accountBalance <= 0){
			Co.println("Insufficient account blanace for trade");
			return 0;
		}
		
		units = 100;
		
		if (accountBalance < units * price){
			Co.println("Insufficient account blanace for trade");
			return 0;
		}
		
		return units;
	}
	
	public int getPositionReentryUnits(double price, Signal signal){
		double accountBalance = account.getAccountBalance();
		int units = 100;
		
		if (accountBalance > units * price){
			return units;
		}

		return 0;
	}
}
