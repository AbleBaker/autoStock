/**
 * 
 */
package com.autoStock.exchange.request;

import com.autoStock.Co;
import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketScannerListener;
import com.autoStock.exchange.results.ExResultMarketOrder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultRowMarketOrder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultSetMarketOrder;
import com.autoStock.exchange.results.ExResultMarketScanner;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultRowMarketScanner;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultSetMarketScanner;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.trading.types.TypePosition;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestMarketScanner {
	private ExResultSetMarketScanner exResultSetMarketScanner;
	private RequestHolder requestHolder;
	
	public RequestMarketScanner(RequestHolder requestHolder){
		this.requestHolder = requestHolder;
		this.requestHolder.caller = this;
		this.exResultSetMarketScanner = new ExResultMarketScanner(). new ExResultSetMarketScanner();
		
		ExchangeController.getIbExchangeInstance().getScanner();
	}
	
	public synchronized void addResult(ExResultRowMarketScanner exResultRowMarketScanner){
		this.exResultSetMarketScanner.listOfExResultRowMarketScanner.add(exResultRowMarketScanner);
	}
	
	public synchronized void finished(){
		((RequestMarketScannerListener)requestHolder.callback).completed(requestHolder, exResultSetMarketScanner);
		Co.println("Finished market scanner...");
	}
}
