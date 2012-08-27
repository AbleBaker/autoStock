/**
 * 
 */
package com.autoStock.exchange.request;

import com.autoStock.Co;
import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketScannerListener;
import com.autoStock.exchange.results.ExResultMarketScanner;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultRowMarketScanner;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultSetMarketScanner;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestMarketScanner {
	private ExResultSetMarketScanner exResultSetMarketScanner;
	private RequestHolder requestHolder;
	private Exchange exchange;
	
	public RequestMarketScanner(RequestHolder requestHolder, Exchange exchange){
		this.requestHolder = requestHolder;
		this.requestHolder.caller = this;
		this.exchange = exchange;
		this.exResultSetMarketScanner = new ExResultMarketScanner(). new ExResultSetMarketScanner();
		
		ExchangeController.getIbExchangeInstance().getScanner(requestHolder, exchange);
	}
	
	public synchronized void addResult(ExResultRowMarketScanner exResultRowMarketScanner){
		this.exResultSetMarketScanner.listOfExResultRowMarketScanner.add(exResultRowMarketScanner);
	}
	
	public synchronized void finished(){
		((RequestMarketScannerListener)requestHolder.callback).completed(requestHolder, exResultSetMarketScanner);
		Co.println("Finished market scanner. Result size:" + exResultSetMarketScanner.listOfExResultRowMarketScanner.size());
	}

	public void clearResults() {
		exResultSetMarketScanner.listOfExResultRowMarketScanner.clear();
	}
	
	public void cancel(){
		ExchangeController.getIbExchangeInstance().cancelScanner(requestHolder);
	}
}
