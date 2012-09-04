package com.autoStock;

import com.autoStock.exchange.request.RequestMarketScanner;
import com.autoStock.exchange.request.RequestMarketScanner.MarketScannerType;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketScannerListener;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultRowMarketScanner;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultSetMarketScanner;
import com.autoStock.internal.Global;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainFilter {
	private Exchange exchange;
	private RequestMarketScanner requestMarketScanner;
	
	public MainFilter(Exchange exchange) {
		this.exchange = exchange;		
		
		Global.callbackLock.requestLock();
		
		requestMarketScanner = new RequestMarketScanner(
			new RequestHolder(new RequestMarketScannerListener(){
				@Override
				public void failed(RequestHolder requestHolder) {
					Co.println("Failed to get market filter");
				}

				@Override
				public void completed(RequestHolder requestHolder, ExResultSetMarketScanner exResultSetMarketScanner, MarketScannerType marketScannerType) {
					Co.println("Got market filter information OK");
					for (ExResultRowMarketScanner exResultRowMarketScanner : exResultSetMarketScanner.listOfExResultRowMarketScanner){
						Co.println("--> Market scanner: " + exResultRowMarketScanner.symbol);
					}
					
					Global.callbackLock.releaseLock();
				}
			}
		), exchange, MarketScannerType.type_percent_gain_open);
	}
}
