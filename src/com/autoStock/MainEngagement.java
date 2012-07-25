/**
 * 
 */
package com.autoStock;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.ExchangeStatusListener;
import com.autoStock.exchange.ExchangeStatusObserver;
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
public class MainEngagement implements RequestMarketScannerListener, ExchangeStatusListener {
	private ExResultSetMarketScanner exResultSetMarketScanner;
	private Exchange exchange;
	private ArrayList<AlgorithmTest> listOfAlgorithm = new ArrayList<AlgorithmTest>();
	private ExchangeStatusObserver exchangeStatusObserver;

	public MainEngagement(Exchange exchange) {
		Global.callbackLock.requestLock(); 
		
		this.exchange = exchange;
		exchangeStatusObserver = new ExchangeStatusObserver(exchange);
		exchangeStatusObserver.addListener(this);
		exchangeStatusObserver.observeExchangeStatus();
	}

	private void engagementStart() {
		new RequestMarketScanner(new RequestHolder(this));
	}
	
	private void engagementWarnStopNear(){
		
	}
	
	private void engagementStop(){
		
	}

	public void handleCompletedMarketScanner() {
		for (ExResultRowMarketScanner result : exResultSetMarketScanner.listOfExResultRowMarketScanner) {
			Co.println("Should run algorithm for symbol: " + result.symbol);
//			new AlgorithmTest(true, exchange);
		}
	}

	@Override
	public void failed(RequestHolder requestHolder) {

	}

	@Override
	public void completed(RequestHolder requestHolder, ExResultSetMarketScanner exResultSetMarketScanner) {
		MainEngagement.this.exResultSetMarketScanner = exResultSetMarketScanner;
		handleCompletedMarketScanner();
	}

	@Override
	public void stateChanged(ExchangeState exchangeState) {
		Co.println("--> Got new state: " + exchangeState.name());
		if (exchangeState == ExchangeState.status_open){
			engagementStart();
		}else if (exchangeState == ExchangeState.status_close_future){
			engagementWarnStopNear();
		}else if (exchangeState == ExchangeState.status_closed){
			engagementStop();
		}
	}
}
