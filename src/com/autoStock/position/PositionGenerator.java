/**
 * 
 */
package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.trading.types.Position;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGenerator {
	
	private Account account;
	private final int MAX_PURCHASE = 25000;
	
	public PositionGenerator(Account account){
		this.account = account;
	}
	
	public Position generatePosition(QuoteSlice typeQuoteSlice, Signal signal, PositionType positionType){
		Position typePosition = new Position();
		typePosition.symbol = typeQuoteSlice.symbol;
		typePosition.price = typeQuoteSlice.priceClose;
		typePosition.lastKnownPrice = typeQuoteSlice.priceClose;
		typePosition.securityType = "STK";
		typePosition.units = (int) getPositionUnits(typePosition.price, signal);
		typePosition.positionType = positionType;
		
		return typePosition;
	}
	
	private double getPositionUnits(double price, Signal signal){
		double accountBalance = account.getBankBalance();
		double units = 0;

		if (accountBalance <= 0){Co.println("Insufficient account blanace for trade"); return 0;}				
		units = Math.min(MAX_PURCHASE / price, account.getBankBalance() / price);
		
		return units;
	}
}
