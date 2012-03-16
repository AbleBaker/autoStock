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
		typePosition.averagePrice = typeQuoteSlice.priceClose;
		typePosition.securityType = "STK";
		typePosition.units = getPositionUnits(signal);
		
		Co.println("Units: " + getPositionUnits(signal));
		
		return typePosition;
	}
	
	private int getPositionUnits(Signal signal){
		if (accountBalance.getBankBalance() <= 0){return 0;}
		return (int) (accountBalance.getBankBalance() / (signal.getSignalMetric(SignalTypeMetric.metric_cci).strength / 100));
	}
}
