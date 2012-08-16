package com.autoStock.algorithm.external;

import java.util.Date;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalControl;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmCondition {
	private static final int maxTransactionsDay = 4;
	private static final double minTakeProfitExit = 1.01d;
	private static final int maxStopLossValue = -50;
	
	public boolean canTadeAfterTransactions(int transactions){
		if (transactions >= maxTransactionsDay){
			return false;
		}
		
		return true;
	}
	public boolean shouldTakeProfit(Position position, QuoteSlice quoteSlice){
		double percentGainFromPosition = 0;
				
		if (position != null && (position.positionType == PositionType.position_long || position.positionType == PositionType.position_short)){
			if (position.price != 0 && position.lastKnownPrice != 0){
				percentGainFromPosition = (position.lastKnownPrice / position.price);
			}
		}
		
		return percentGainFromPosition >= minTakeProfitExit;
	}
	
	public boolean shouldStopLoss(Position position){
		double transactionCostTotal = 0;
		double valueGainFromPosition = 0;
		
		transactionCostTotal += Account.instance.getTransactionCost(position.units, position.price);
		transactionCostTotal += Account.instance.getTransactionCost(position.units, position.lastKnownPrice);
		
		valueGainFromPosition = (position.lastKnownPrice * position.units) - (position.price * position.units);
		valueGainFromPosition -= transactionCostTotal;
		
//		Co.println("--> Value gain: " + valueGainFromPosition + (valueGainFromPosition < maxStopLossValue));
		
		return valueGainFromPosition < maxStopLossValue;
	}
	
	public boolean canTradeOnDate(Date date, Exchange exchange){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), ExternalConditionDefintions.maxPositionEntryTime);		
	
		if (date.getHours() > dateForLastExecution.getHours() || ( date.getHours() >= dateForLastExecution.getHours() && date.getMinutes() >= dateForLastExecution.getMinutes())){
			return false;
		}
		
		return true;
	}
	
	public boolean shouldRequestExitOnDate(Date date, Exchange exchange, Position position){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), ExternalConditionDefintions.maxPositionExitTime);		
		if (date.getHours() > dateForLastExecution.getHours() || (date.getHours() >= dateForLastExecution.getHours() && date.getMinutes() >= dateForLastExecution.getMinutes())){
			return true;
		}
		
		return false;
	}
}
