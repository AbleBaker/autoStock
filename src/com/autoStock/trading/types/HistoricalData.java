package com.autoStock.trading.types;

import java.util.Date;

import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class HistoricalData implements Cloneable {
	public Exchange exchange;
	public Symbol symbol;
	public Date startDate;
	public Date endDate;
	public Resolution resolution;
	public long duration;
	
	public HistoricalData(Exchange exchange, Symbol symbol, Date startDate, Date endDate, Resolution resolution){
		this.exchange = exchange;
		this.symbol = symbol;
		this.startDate = startDate;
		this.endDate = endDate;
		this.resolution = resolution;
		this.duration = (endDate.getTime() / 1000 - startDate.getTime() / 1000);
	}
	
	@Override
	public HistoricalData clone(){
		try {
			return (HistoricalData) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			ApplicationStates.shutdown();
			return null;
		}
	}
}
