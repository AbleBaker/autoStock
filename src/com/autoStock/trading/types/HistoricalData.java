package com.autoStock.trading.types;

import java.util.Date;

import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;

/**
 * @author Kevin Kowalewski
 *
 */
public class HistoricalData implements Cloneable {
	public Date startDate;
	public Date endDate;
	public String symbol;
	public Resolution resolution;
	public String securityType;
	public long duration;
	
	public HistoricalData(String symbol, String securityType, Date startDate, Date endDate, Resolution resolution){
		if (symbol != null){this.symbol = symbol.toUpperCase();}
		this.securityType = securityType.toUpperCase();
		this.startDate = startDate;
		this.endDate = endDate;
		this.resolution = resolution;
		this.duration = (endDate.getTime() / 1000 - startDate.getTime() / 1000);
	}
	
	public HistoricalData clone(){
		try {
			return (HistoricalData) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
