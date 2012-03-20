/**
 * 
 */
package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.balance.AccountBalance;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;
import com.autoStock.trading.types.TypePosition;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGenerator {
	
	private AccountBalance accountBalance;
	
	public PositionGenerator(AccountBalance accountBalance){
		this.accountBalance = accountBalance;
	}
	
	public TypePosition generatePosition(TypeQuoteSlice typeQuoteSlice, Signal signal){
		TypePosition typePosition = new TypePosition();
		typePosition.symbol = typeQuoteSlice.symbol;
		typePosition.pricePosition = typeQuoteSlice.priceClose;
		typePosition.securityType = "STK";
		typePosition.units = (int) getPositionUnits(typePosition.pricePosition, signal);
		
		return typePosition;
	}
	
	private double getPositionUnits(double price, Signal signal){
		if (accountBalance.getBankBalance() <= 0){Co.println("Insufficient account blanace for trade"); return 0;}
		return ((accountBalance.getBankBalance() / price) * ((double)signal.getCombinedSignal() / 100));
	}
}
