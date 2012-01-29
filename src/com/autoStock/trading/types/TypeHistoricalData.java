package com.autoStock.trading.types;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class TypeHistoricalData {
	public Date startDate;
	public Date endDate;
	public String symbol;
	
	public TypeHistoricalData(String symbol, Date startDate, Date endDate){
		this.symbol = symbol;
		this.startDate = startDate;
		this.endDate = endDate;
	}
}
