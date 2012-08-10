package com.autoStock.algorithm.external;

import java.util.Date;

import com.autoStock.Co;
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
	public boolean canTradeOnDate(Date date, Exchange exchange){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), ExternalConditionDefintions.maxPositionEntryTime);		
	
		if (date.getHours() > dateForLastExecution.getHours() || (
			date.getHours() >= dateForLastExecution.getHours() && 
			date.getMinutes() >= dateForLastExecution.getMinutes())){
			
			return false;
		}
		
		return true;
	}
	
	public boolean shouldRequestExit(Date date, Exchange exchange, Position position){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), ExternalConditionDefintions.maxPositionExitTime);		
		if (date.getHours() > dateForLastExecution.getHours() || (
			date.getHours() >= dateForLastExecution.getHours() && date.getMinutes() >= dateForLastExecution.getMinutes())){
			return true;
		}
		return false;
	}
}
