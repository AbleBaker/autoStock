/**
 * 
 */
package com.autoStock.display;

import com.autoStock.Co;
import com.autoStock.exchange.request.RequestMarketIndexData;
import com.autoStock.exchange.request.RequestMarketSymbolData;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketIndexDataListener;
import com.autoStock.exchange.request.listener.RequestMarketSymbolDataListener;
import com.autoStock.exchange.results.ExResultMarketSymbolData.ExResultSetMarketSymbolData;
import com.autoStock.exchange.results.ExResultMarketIndexData.ExResultSetMarketIndexData;
import com.autoStock.internal.Global;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Period;
import com.autoStock.trading.types.MarketIndexData;
import com.autoStock.trading.types.MarketSymbolData;
import com.autoStock.types.IndexSlice;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class DisplayMarketIndexData {
	private MarketIndexData marketIndexData;
	
	public DisplayMarketIndexData(MarketIndexData marketIndexData){
		this.marketIndexData = marketIndexData;
		Global.callbackLock.requestLock();
	}
	
	public void display(){
		new RequestMarketIndexData(new RequestHolder(null), new RequestMarketIndexDataListener() {
			
			@Override
			public void failed(RequestHolder requestHolder) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void receiveIndexSlice(RequestHolder requestHolder, IndexSlice indexSlice) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetMarketIndexData exResultSetIndexData) {
				// TODO Auto-generated method stub
				
			}
		}, marketIndexData, Period.min.seconds * 1000);
	}
}
