package com.autoStock.algorithm.external;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.finance.Account;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmCondition {
	private StrategyOptions strategyOptions;
	
	public AlgorithmCondition(StrategyOptions strategyOptions){
		this.strategyOptions = strategyOptions;
	}
	
	public boolean canTadeAfterTransactions(int transactions){
		if (transactions >= strategyOptions.maxTransactionsDay){
			return false;
		}
		
		return true;
	}
	
	public boolean canTradeOnDate(Date date, Exchange exchange){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), strategyOptions.maxPositionEntryTime);		
	
		if (date.getHours() > dateForLastExecution.getHours() || ( date.getHours() >= dateForLastExecution.getHours() && date.getMinutes() >= dateForLastExecution.getMinutes())){
			return false;
		}
		
		return true;
	}
	
	public boolean taperPeriodLengthLower(Date date, Exchange exchange){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), strategyOptions.maxPositionTaperTime);		
		if (date.getHours() > dateForLastExecution.getHours() || (date.getHours() >= dateForLastExecution.getHours() && date.getMinutes() >= dateForLastExecution.getMinutes())){
			return true;
		}
		
		return false;
	}
	
	public boolean takeProfit(Position position, QuoteSlice quoteSlice){
		double percentGainFromPosition = 0;
		
		if (position != null && (position.positionType == PositionType.position_long || position.positionType == PositionType.position_short)){
			if (position.price != 0 && position.lastKnownPrice != 0){
				percentGainFromPosition = (position.lastKnownPrice / position.price);
			}
		}
		
		return percentGainFromPosition >= strategyOptions.minTakeProfitExit;
	}
	
	public boolean stopLoss(Position position){
		double transactionCostTotal = 0;
		double valueGainFromPosition = 0;
		
		transactionCostTotal += Account.instance.getTransactionCost(position.units, position.price);
		transactionCostTotal += Account.instance.getTransactionCost(position.units, position.lastKnownPrice);
		
		valueGainFromPosition = (position.lastKnownPrice * position.units) - (position.price * position.units);
		valueGainFromPosition -= transactionCostTotal;
		
//		Co.println("--> Value gain: " + valueGainFromPosition + (valueGainFromPosition < maxStopLossValue));
		
		return valueGainFromPosition < strategyOptions.maxStopLossValue;
	}
	
	public boolean requestExitOnDate(Date date, Exchange exchange){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), strategyOptions.maxPositionExitTime);		
		if (date.getHours() > dateForLastExecution.getHours() || (date.getHours() >= dateForLastExecution.getHours() && date.getMinutes() >= dateForLastExecution.getMinutes())){
			return true;
		}
		
		return false;
	}

	public boolean disableAfterNilChanges(ArrayList<QuoteSlice> listOfQuoteSlice) {
		int countOfNilChanges = 0;
		for (int i=0; i < listOfQuoteSlice.size() -2; i++){
			QuoteSlice curremtQuoteSlice = listOfQuoteSlice.get(i);
			QuoteSlice nextQuoteSlice = listOfQuoteSlice.get(i+1);
			
			if (curremtQuoteSlice.priceClose == nextQuoteSlice.priceClose){
				countOfNilChanges++;
			}else{
				countOfNilChanges = 0;
			}
		}
		return countOfNilChanges > strategyOptions.maxNilChanges;
	}
}
