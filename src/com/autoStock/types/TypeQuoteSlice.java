/**
 * 
 */
package com.autoStock.types;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.exchange.results.ExResultRealtimeData.ExResultRowRealtimeData;
import com.autoStock.trading.types.TypeRealtimeData;

/**
 * @author Kevin Kowalewski
 *
 */
public class TypeQuoteSlice {
	public String symbol;
	public float priceOpen;
	public float priceHigh;
	public float priceLow;
	public float priceClose;
	public float priceBid;
	public float priceAsk;
	public int sizeVolume;
	public Date dateTime;
	
	public TypeQuoteSlice(){
		
	}
	
	public TypeQuoteSlice(String symbol, float priceOpen, float priceHigh, float priceLow, float priceClose, float priceBid, float priceAsk, int sizeVolume, Date dateTime) {
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
