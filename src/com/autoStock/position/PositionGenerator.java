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
	private final int positionMaximumPrice = 10000;
	
	public PositionGenerator(Account account){
		this.account = account;
	}
	
	public Position generatePosition(QuoteSlice typeQuoteSlice, Signal signal, PositionType positionType){
		Position position = new Position();
		position.symbol = typeQuoteSlice.symbol;
		position.price = typeQuoteSlice.priceClose;
		position.lastKnownPrice = typeQuoteSlice.priceClose;
		position.securityType = "STK";
		position.units = (int) getPositionUnits(position.price, signal);
		position.positionType = positionType;
		
		return position;
	}
	
	private double getPositionUnits(double price, Signal signal){
		double accountBalance = account.getBankBalance();
		double units = 0;

		if (accountBalance <= 0){
			Co.println("Insufficient account blanace for trade");
			return 0;
		}				
		units = Math.min(positionMaximumPrice / price, account.getBankBalance() / price);
		
		return units;
	}
}
