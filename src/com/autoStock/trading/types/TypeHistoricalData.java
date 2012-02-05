package com.autoStock.trading.types;

import java.util.Date;

import com.autoStock.trading.platform.ib.definitions.HistoricalData.Resolution;

/**
 * @author Kevin Kowalewski
 *
 */
public class TypeHistoricalData implements Cloneable {
	public Date startDate;
	public Date endDate;
	public String symbol;
	public Resolution resolution;
	public String securityType;
	public long duration;
	
	public TypeHistoricalData(String symbol, String securityType, Date startDate, Date endDate, Resolution resolution){
		this.symbol = symbol.toUpperCase();
		this.securityType = securityType.toUpperCase();
		this.startDate = startDate;
		this.endDate = endDate;
		this.resolution = resolution;
		this.duration = (endDate.getTime() / 1000 - startDate.getTime() / 1000);
	}
	
	public TypeHistoricalData clone(){
		try {
			return (TypeHistoricalData) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
