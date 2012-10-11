/**
 * 
 */
package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.finance.Account;
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
	private Account account = Account.getInstance();
	private final int positionMaximumPrice = 5000;
	
	public Position generatePosition(QuoteSlice quoteSlice, Signal signal, PositionType positionType, Exchange exchange){
		int positionUnits = (int) getPositionUnits(quoteSlice.priceClose, signal);
		
		if (positionUnits != 0){
			return new Position(positionType, positionUnits, quoteSlice.symbol, exchange, "STK", quoteSlice.priceClose);
		}
		
		return null;
	}
	
	private double getPositionUnits(double price, Signal signal){
		double accountBalance = account.getAccountBalance();
		double units = 0;

		if (accountBalance <= 0){
			Co.println("Insufficient account blanace for trade");
			return 0;
		}
		
		units = Math.min(positionMaximumPrice / price, account.getAccountBalance() / price);
		
		return units;
	}
}
