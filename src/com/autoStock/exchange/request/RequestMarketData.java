/**
 * 
 */
package com.autoStock.exchange.request;

import java.util.Date;

import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.exchange.results.ExResultMarketData;
import com.autoStock.exchange.results.ExResultMarketData.ExResultRowMarketData;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.tools.QuoteSliceTools;
import com.autoStock.trading.platform.ib.definitions.MarketDataDefinitions.TickTypes;
import com.autoStock.trading.types.MarketData;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * 
 */
public class RequestMarketData {
	public RequestHolder requestHolder;
	public RequestMarketDataListener requestMarketDataListener;
	public ExResultSetMarketData exResultSetMarketData;
	public MarketData typeMarketData;
	private Thread threadForSliceCollector;
	private int sliceMilliseconds;
	private long receivedTimestamp = 0;
	private Date sliceDate;

	public RequestMarketData(RequestHolder requestHolder, RequestMarketDataListener requestMarketDataListener, MarketData typeMarketData, int sliceMilliseconds) {
		this.requestHolder = requestHolder;
		this.requestHolder.caller = this;
		this.requestMarketDataListener = requestMarketDataListener;
		this.typeMarketData = typeMarketData;
		this.exResultSetMarketData = new ExResultMarketData(). new ExResultSetMarketData(typeMarketData);
		this.sliceMilliseconds = sliceMilliseconds;
		
		ExchangeController.getIbExchangeInstance().getMarketData(typeMarketData, requestHolder);
	}
	
	public synchronized void addResult(ExResultRowMarketData exResultRowMarketData){
		if (exResultRowMarketData.tickType == TickTypes.type_string){
			if (sliceMilliseconds != 0 && receivedTimestamp == 0){
				receivedTimestamp = Long.valueOf(exResultRowMarketData.tickStringValue);
				runThreadForSliceCollector(sliceMilliseconds);
			}
		}else if (receivedTimestamp != 0){
			synchronized(RequestMarketData.this){
				exResultSetMarketData.listOfExResultRowMarketData.add(exResultRowMarketData);
			}
		} 
	}
	
	public void runThreadForSliceCollector(final int sliceMilliseconds){
		Date date = new Date(receivedTimestamp*1000);
		//Co.println("*********************************************: " + date.toGMTString());
		
		this.threadForSliceCollector = new Thread(new Runnable(){
			@Override
			public void run() {
				while (true){
					try {Thread.sleep(sliceMilliseconds);}catch(InterruptedException e){return;}
					synchronized(RequestMarketData.this){
						QuoteSlice typeQuoteSlice = new QuoteSliceTools().getQuoteSlice(exResultSetMarketData.listOfExResultRowMarketData);
						exResultSetMarketData.listOfExResultRowMarketData.clear();
						
						//Co.println("Generated new QuoteSlice");
						//Co.println("O,H,L,C" + typeQuoteSlice.priceOpen + "," + typeQuoteSlice.priceHigh + "," + typeQuoteSlice.priceLow + "," + typeQuoteSlice.priceClose + "," + typeQuoteSlice.sizeVolume);
						
						requestMarketDataListener.receiveQuoteSlice(requestHolder, typeQuoteSlice);
					}
				}
			}
		});
		
		this.threadForSliceCollector.start();
	}
}
