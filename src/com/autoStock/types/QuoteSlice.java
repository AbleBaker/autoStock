/**
 * 
 */
package com.autoStock.types;

import java.util.Date;

import com.autoStock.internal.ApplicationStates;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class QuoteSlice implements Cloneable {
	public String symbol;
	public double priceOpen;
	public double priceHigh;
	public double priceLow;
	public double priceClose;
	public double priceBid;
	public double priceAsk;
	public int sizeVolume;
	public Date dateTime = new Date();
	public Resolution resolution;
	
	public QuoteSlice(){
		
	}
	
	public QuoteSlice(String symbol, double priceOpen, double priceHigh, double priceLow, double priceClose, double priceBid, double priceAsk, int sizeVolume, Date dateTime, Resolution resolution) {
		this.symbol = symbol;
		this.priceOpen = priceOpen;
		this.priceHigh = priceHigh;
		this.priceLow = priceLow;
		this.priceClose = priceClose;
		this.priceBid = priceBid;
		this.priceAsk = priceAsk;
		this.sizeVolume = sizeVolume;
		this.dateTime = dateTime;
	}
	
	@Override
	public QuoteSlice clone(){
		try {
			return (QuoteSlice) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			ApplicationStates.shutdown();
			return null;
		}
	}
}
