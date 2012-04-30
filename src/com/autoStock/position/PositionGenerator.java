/**
 * 
 */
package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.signal.Signal;
import com.autoStock.trading.types.TypePosition;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGenerator {
	
	private Account account;
	
	public PositionGenerator(Account account){
		this.account = account;
	}
	
	public TypePosition generatePosition(TypeQuoteSlice typeQuoteSlice, Signal signal){
		TypePosition typePosition = new TypePosition();
		typePosition.symbol = typeQuoteSlice.symbol;
		typePosition.price = typeQuoteSlice.priceClose;
		typePosition.securityType = "STK";
		typePosition.units = (int) getPositionUnits(typePosition.price, signal);
		
		return typePosition;
	}
	
	private double getPositionUnits(double price, Signal signal){
		double accountBalance = account.getBankBalance();
		double units = 0;

		if (accountBalance <= 0){Co.println("Insufficient account blanace for trade"); return 0;}		
		//units = Math.min(1000, (account.getBankBalance() / price)) * ((double)signal.getCombinedSignal() / 100);
		
		units = account.getBankBalance() / price;
		
		return units;
	}
}
