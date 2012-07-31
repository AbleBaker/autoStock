/**
 * 
 */
package com.autoStock.exchange.request;

import java.util.ArrayList;
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
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

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
	private ArrayList<QuoteSlice> listOfQuoteSliceRecall = new ArrayList<QuoteSlice>();
	private Exchange exchange;
	private Symbol symbol;

	public RequestMarketData(RequestHolder requestHolder, RequestMarketDataListener requestMarketDataListener, Exchange exchange, Symbol symbol, int sliceMilliseconds) {
		this.requestHolder = requestHolder;
		this.requestHolder.caller = this;
		this.requestMarketDataListener = requestMarketDataListener;
		this.exResultSetMarketData = new ExResultMarketData(). new ExResultSetMarketData(typeMarketData);
		this.sliceMilliseconds = sliceMilliseconds;
		this.exchange = exchange;
		this.symbol = symbol;
		
		ExchangeController.getIbExchangeInstance().getMarketData(exchange, symbol, requestHolder);
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
		
		threadForSliceCollector = new Thread(new Runnable(){
			@Override
			public void run() {
				while (true){
					try {Thread.sleep(sliceMilliseconds);}catch(InterruptedException e){return;}
					
					synchronized(RequestMarketData.this){
						QuoteSlice quoteSlice = new QuoteSliceTools().getQuoteSlice(exResultSetMarketData.listOfExResultRowMarketData, symbol);
						
						if (exResultSetMarketData.listOfExResultRowMarketData.size() > 60){ //hack
							exResultSetMarketData.listOfExResultRowMarketData.remove(0);
						}
						
//						exResultSetMarketData.listOfExResultRowMarketData.clear();
						//Co.println("O,H,L,C" + typeQuoteSlice.priceOpen + "," + typeQuoteSlice.priceHigh + "," + typeQuoteSlice.priceLow + "," + typeQuoteSlice.priceClose + "," + typeQuoteSlice.sizeVolume);	
						requestMarketDataListener.receiveQuoteSlice(requestHolder, quoteSlice);
					}
				}
			}
		});
		
		this.threadForSliceCollector.start();
	}
}
