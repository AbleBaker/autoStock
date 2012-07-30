/**
 * 
 */
package com.autoStock;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.algorithm.ActiveAlgorithmContainer;
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
import com.autoStock.types.Symbol;
import com.autoStock.types.basic.Time;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainEngagement implements RequestMarketScannerListener, ExchangeStatusListener {
	private ExResultSetMarketScanner exResultSetMarketScanner;
	private Exchange exchange;
	private ArrayList<ActiveAlgorithmContainer> listOfActiveAlgorithmContainer = new ArrayList<ActiveAlgorithmContainer>();
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
	
	private void engagementWarn(){
		
	}
	
	private void engagementStop(){
		
	}

	public synchronized void handleCompletedMarketScanner() {
		for (ExResultRowMarketScanner result : exResultSetMarketScanner.listOfExResultRowMarketScanner) {
			if (getAlgorithmContainerForSymbol(result.symbol) == null){
				Co.println("Will run algorithm for symbol: " + result.symbol);
				ActiveAlgorithmContainer container = new ActiveAlgorithmContainer(false, exchange, new Symbol(result.symbol));
				container.activate();
				listOfActiveAlgorithmContainer.add(container);
			}else{
//				Co.println("--> Already running algorithm on: " + result.symbol);
			}
		}
	}
	
	private ActiveAlgorithmContainer getAlgorithmContainerForSymbol(String symbol){
		for (ActiveAlgorithmContainer container : listOfActiveAlgorithmContainer){
			if (container.symbol.symbol.equals(symbol)){
				return container;
			}
		}
		
		return null;
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
			engagementWarn();
		}else if (exchangeState == ExchangeState.status_closed){
			engagementStop();
		}
	}
}
