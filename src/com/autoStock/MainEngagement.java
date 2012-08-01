/**
 * 
 */
package com.autoStock;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.algorithm.ActiveAlgorithmContainer;
import com.autoStock.algorithm.AlgorithmManager;
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
import com.autoStock.finance.Account;
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
	private ExchangeStatusObserver exchangeStatusObserver;
	private AlgorithmManager algorithmManager = new AlgorithmManager();

	public MainEngagement(Exchange exchange) {
		Global.callbackLock.requestLock(); 
		
		this.exchange = exchange;
		exchangeStatusObserver = new ExchangeStatusObserver(exchange);
		exchangeStatusObserver.addListener(this);
		exchangeStatusObserver.observeExchangeStatus();
		
		algorithmManager.initalize();
	}

	private void engagementStart() {
		new RequestMarketScanner(new RequestHolder(this));
	}
	
	private void engagementWarn(ExchangeState exchangeState){
		Co.println("--> Received warning: " + exchangeState.timeUntilFuture.minutes + ":" + exchangeState.timeUntilFuture.seconds);
		algorithmManager.warnAll(exchangeState);
	}
	
	private void engagementStop(){
		Co.println("Balance: " + Account.instance.getBankBalance());
		Co.println("Trasactions and fees: " + Account.instance.getTransactions() + ", " + Account.instance.getTransactionFeesPaid());
	}

	public synchronized void handleCompletedMarketScanner() {
		ArrayList<String> listOfString = new ArrayList<String>();
		for (ExResultRowMarketScanner result : exResultSetMarketScanner.listOfExResultRowMarketScanner) {
			listOfString.add(result.symbol);
		}
		
		algorithmManager.setListOfSymbols(listOfString, exchange);
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
		}else if (exchangeState == ExchangeState.status_close_future || exchangeState == ExchangeState.status_open_future){
			engagementWarn(exchangeState);
		}else if (exchangeState == ExchangeState.status_closed){
			engagementStop();
		}
	}
}
