/**
 * 
 */
package com.autoStock.exchange.request;

import java.util.Date;

import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketSymbolDataListener;
import com.autoStock.exchange.results.ExResultMarketData;
import com.autoStock.exchange.results.ExResultMarketData.ExResultRowMarketData;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketSymbolData;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.QuoteSliceTools;
import com.autoStock.trading.platform.ib.definitions.MarketDataDefinitions.TickTypes;
import com.autoStock.trading.types.MarketSymbolData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Index;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class RequestIndexData {
	public RequestHolder requestHolder;
	public RequestMarketSymbolDataListener requestMarketDataListener;
	public ExResultSetMarketSymbolData exResultSetMarketData;
	public MarketSymbolData typeMarketData;
	private Thread threadForSliceCollector;
	private int sliceMilliseconds;
	private long receivedTimestamp = 0;
	private Exchange exchange;
	private Symbol symbol;
	private QuoteSlice quoteSlicePrevious = new QuoteSlice();
	private boolean isCancelled = false;

	public RequestIndexData(RequestHolder requestHolder, RequestMarketSymbolDataListener requestMarketDataListener, Exchange exchange, Symbol symbol, int sliceMilliseconds) {
		this.requestHolder = requestHolder;
		this.requestHolder.caller = this;
		this.requestMarketDataListener = requestMarketDataListener;
		this.exResultSetMarketData = new ExResultMarketData(). new ExResultSetMarketSymbolData(typeMarketData);
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
		}
		exResultSetMarketData.listOfExResultRowMarketData.add(exResultRowMarketData);
	}
	
	public void runThreadForSliceCollector(final int sliceMilliseconds){
		Date date = new Date(receivedTimestamp*1000);
		
		threadForSliceCollector = new Thread(new Runnable(){
			@Override
			public void run() {
				while (true){
					try {Thread.sleep(sliceMilliseconds);}catch(InterruptedException e){return;}
					
					synchronized(RequestIndexData.this){
						QuoteSlice quoteSlice = new QuoteSliceTools().getQuoteSlice(exResultSetMarketData.listOfExResultRowMarketData, symbol);
						new QuoteSliceTools().mergeQuoteSlices(quoteSlicePrevious, quoteSlice);
						quoteSlice.dateTime = DateTools.getForeignDateFromLocalTime(DateTools.getTimeFromDate(new Date()), exchange.timeZone);
						
						quoteSlicePrevious = quoteSlice;
						
						exResultSetMarketData.listOfExResultRowMarketData.clear();
//						Co.println("O,H,L,C,V: " + quoteSlice.priceOpen + ", " + quoteSlice.priceHigh + ", " + quoteSlice.priceLow + ", " + quoteSlice.priceClose + ", " + quoteSlice.sizeVolume);
						requestMarketDataListener.receiveQuoteSlice(requestHolder, quoteSlice);
					}
				}
			}
		});
		
		this.threadForSliceCollector.start();
	}
	
	public void cancel(){
		if (threadForSliceCollector != null && isCancelled == false){
			threadForSliceCollector.interrupt();
			ExchangeController.getIbExchangeInstance().cancelMarketData(requestHolder);
			isCancelled = true;
		}
	}
}
