/**
 * 
 */
package com.autoStock;

import java.util.Date;

import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.RequestMarketData;
import com.autoStock.exchange.request.RequestMarketScanner;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.exchange.request.listener.RequestMarketScannerListener;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultRowMarketScanner;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultSetMarketScanner;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.position.PositionManager;
import com.autoStock.trading.types.MarketData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainEngagement implements RequestMarketScannerListener {
	private ExResultSetMarketScanner exResultSetMarketScanner;
	private Exchange exchange;

	public MainEngagement(Exchange exchange) {
		this.exchange = exchange;
		
		Global.callbackLock.requestLock(); 

		while (exchange.isOpen() == false) {
			Co.println("--> Waiting for exchange to open...");
		}
		
		init();

//		Global.callbackLock.releaseLock();
	}

	public void init() {
		dayStart();
	}

	public void dayStart() {
		new RequestMarketScanner(new RequestHolder(this));
	}

	public void handleCompletedMarketScanner() {
		for (ExResultRowMarketScanner result : exResultSetMarketScanner.listOfExResultRowMarketScanner) {
			Co.println("Should run algorithm for symbol: " + result.symbol);
			
//			new AlgorithmTest(true, exchange);
		}
	}

	public void dayEnd() {
		Co.println("End of day reached, sell all...");
		PositionManager.instance.executeSellAll();
	}

	@Override
	public void failed(RequestHolder requestHolder) {

	}

	@Override
	public void completed(RequestHolder requestHolder, ExResultSetMarketScanner exResultSetMarketScanner) {
		MainEngagement.this.exResultSetMarketScanner = exResultSetMarketScanner;
		ExchangeController.getIbExchangeInstance().ibExchangeClientSocket.eClientSocket.cancelScannerSubscription(requestHolder.requestId);

		handleCompletedMarketScanner();
	}

}
