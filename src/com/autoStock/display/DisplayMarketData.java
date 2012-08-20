/**
 * 
 */
package com.autoStock.display;

import com.autoStock.Co;
import com.autoStock.exchange.request.RequestMarketData;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Period;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.MarketData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class DisplayMarketData {
	private MarketData marketData;
	
	public DisplayMarketData(MarketData marketData){
		this.marketData = marketData;
		Global.callbackLock.requestLock();
	}
	
	public void display(){
		new RequestMarketData(new RequestHolder(null), new RequestMarketDataListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetMarketData exResultSetMarketData) {
				Co.println("Completed!");
				
			}

			@Override
			public void receiveQuoteSlice(RequestHolder requestHolder, QuoteSlice typeQuoteSlice) {
				// TODO Auto-generated method stub
				
			}
		}, marketData.exchange, marketData.symbol, Period.min.seconds * 1000);
	}
}
