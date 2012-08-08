package com.autoStock.algorithm.external;

import java.util.Date;

import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmCondition {	
	@SuppressWarnings("deprecation")
	public boolean canTradeOnDate(QuoteSlice quoteSlice, Exchange exchange){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeClose), AlgorithmConditionDefintions.maxPositionEntryTime);		
	
		if (quoteSlice.dateTime.getHours() > dateForLastExecution.getHours() || (
			quoteSlice.dateTime.getHours() >= dateForLastExecution.getHours() && quoteSlice.dateTime.getMinutes() >= dateForLastExecution.getMinutes())){
			return false;
		}
		
		return true;
	}
	
	public boolean shouldRequestExit(QuoteSlice quoteSlice, Exchange exchange, Position position){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeClose), AlgorithmConditionDefintions.maxPositionExitTime);		
		if (quoteSlice.dateTime.getHours() > dateForLastExecution.getHours() || (
			quoteSlice.dateTime.getHours() >= dateForLastExecution.getHours() && quoteSlice.dateTime.getMinutes() >= dateForLastExecution.getMinutes())){
			return true;
		}
		return false;
	}
}
