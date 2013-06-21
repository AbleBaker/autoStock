/**
 * 
 */
package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.account.BasicAccount;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGenerator {
	private boolean throwOnInsufficientBalance = true;
	
	public Position generatePosition(QuoteSlice quoteSlice, Signal signal, PositionType positionType, Exchange exchange, PositionOptions positionOptions, BasicAccount basicAccount){
		int positionUnits = getPositionInitialUnits(quoteSlice.priceClose, signal, basicAccount);
		
		if (positionUnits != 0){
			return new Position(positionType, positionUnits, quoteSlice.symbol, exchange, quoteSlice.priceClose, positionOptions, basicAccount);
		}
		
		return null;
	}
	
	private int getPositionInitialUnits(double price, Signal signal, BasicAccount basicAccount){
		double accountBalance = basicAccount.getBalance();
		int units = 100;

		if (accountBalance <= 0 || accountBalance < units * price){
			Co.println("Insufficient account blanace for trade");
			if (throwOnInsufficientBalance){
				throw new IllegalStateException();
			}
			return 0;
		}
		
		return units;
	}
	
	public int getPositionReentryUnits(double price, Signal signal, BasicAccount basicAccount){
		double accountBalance = basicAccount.getBalance();
		int units = 100;

		if (accountBalance <= 0 || accountBalance < units * price){
			Co.println("Insufficient account blanace for trade");
			if (throwOnInsufficientBalance){
				throw new IllegalStateException();
			}
			return 0;
		}
		
		return units;
	}
}
