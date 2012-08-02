package com.autoStock.algorithm.external;

import java.util.Date;

import com.autoStock.tools.DateTools;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmCondition {	
	@SuppressWarnings("deprecation")
	public static boolean canTradeOnDate(QuoteSlice quoteSlice, Exchange exchange){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeClose), AlgorithmConditionDefintions.maxPositionEntryTime);		
	
		if (quoteSlice.dateTime.getHours() > dateForLastExecution.getHours() || (
			quoteSlice.dateTime.getHours() >= dateForLastExecution.getHours() && quoteSlice.dateTime.getMinutes() >= dateForLastExecution.getMinutes())){
			return false;
		}
		
		return true;
	}
}
