/**
 * 
 */
package com.autoStock.types;

import java.util.Date;

import com.autoStock.trading.platform.ib.definitions.HistoricalData.Resolution;

/**
 * @author Kevin Kowalewski
 *
 */
public class TypeQuoteSlice {
	public String symbol;
	public double priceOpen;
	public double priceHigh;
	public double priceLow;
	public double priceClose;
	public double priceBid;
	public double priceAsk;
	public int sizeVolume;
	public Date dateTime;
	public Resolution resolution;
	
	public TypeQuoteSlice(){
		
	}
	
	public TypeQuoteSlice(String symbol, double priceOpen, double priceHigh, double priceLow, double priceClose, double priceBid, double priceAsk, int sizeVolume, Date dateTime, Resolution resolution) {
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
}
