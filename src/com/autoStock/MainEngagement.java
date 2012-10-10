/**
 * 
 */
package com.autoStock;

import java.util.ArrayList;

import com.autoStock.algorithm.core.AlgorithmManager;
import com.autoStock.algorithm.external.ExternalConditionDefintions;
import com.autoStock.exchange.ExchangeStatusListener;
import com.autoStock.exchange.ExchangeStatusObserver;
import com.autoStock.exchange.request.MultipleRequestMarketScanner;
import com.autoStock.exchange.request.RequestMarketScanner.MarketScannerType;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.MultipleRequestMarketScannerListener;
import com.autoStock.exchange.results.MultipleResultMarketScanner.MultipleResultRowMarketScanner;
import com.autoStock.exchange.results.MultipleResultMarketScanner.MultipleResultSetMarketScanner;
import com.autoStock.internal.Global;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainEngagement implements MultipleRequestMarketScannerListener, ExchangeStatusListener {
	private Exchange exchange;
	private ExchangeStatusObserver exchangeStatusObserver;
	private AlgorithmManager algorithmManager = new AlgorithmManager();
	private MultipleRequestMarketScanner multipleRequestMarketScanner = new MultipleRequestMarketScanner(this);

	public MainEngagement(Exchange exchange) {
		Global.callbackLock.requestLock();

		this.exchange = exchange;
		exchangeStatusObserver = new ExchangeStatusObserver(exchange);
		exchangeStatusObserver.addListener(this);
		exchangeStatusObserver.observeExchangeStatus();
		
		algorithmManager.initalize();
	}

	private void engagementStart() {
		multipleRequestMarketScanner.addRequest(exchange, MarketScannerType.type_percent_gain_open);
		multipleRequestMarketScanner.addRequest(exchange, MarketScannerType.type_percent_gain);
		multipleRequestMarketScanner.addRequest(exchange, MarketScannerType.type_high_open_gap);
//		multipleRequestMarketScanner.addRequest(exchange, MarketScannerType.type_implied_volatility_gain);
//		multipleRequestMarketScanner.addRequest(exchange, MarketScannerType.type_hot_by_price);
		multipleRequestMarketScanner.addRequest(exchange, MarketScannerType.type_most_active);
//		multipleRequestMarketScanner.addRequest(exchange, MarketScannerType.type_top_trade_rate);
		multipleRequestMarketScanner.addRequest(exchange, MarketScannerType.type_hot_by_volume);
		
		multipleRequestMarketScanner.startScanners();
	}
	
	private void engagementWarn(ExchangeState exchangeState){
		Co.println("--> Received warning: " + exchangeState.timeUntilFuture.hours + ":" + exchangeState.timeUntilFuture.minutes + ":" + exchangeState.timeUntilFuture.seconds);
		if (exchangeState == ExchangeState.status_close_future && exchangeState.timeUntilFuture.hours == 0 && exchangeState.timeUntilFuture.minutes <= ExternalConditionDefintions.maxScannerExitMinutes){
			multipleRequestMarketScanner.stopScanner();
		}
		algorithmManager.warnAll(exchangeState);
	}
	
	private void engagementStop(){
		Co.println("--> Received stop");
		ArrayList<ArrayList<String>> listOfAlgorithmManagerRows = (ArrayList<ArrayList<String>>) algorithmManager.getAlgorithmTable().clone();
		algorithmManager.stopAll();
		algorithmManager.displayEndOfDayStats(listOfAlgorithmManagerRows);
		Global.callbackLock.releaseLock();
	}

	public synchronized void handleCompletedMarketScanner(MultipleResultSetMarketScanner multipleResultSetMarketScanner) {
		ArrayList<String> listOfString = new ArrayList<String>();
//		for (MultipleResultRowMarketScanner result : multipleResultSetMarketScanner.listOfMultipleResultRowMarketScanner){
////			Co.println("Scanner retrieved: " + result.marketScannerType.name() + ", " + result.symbol);
//			listOfString.add(result.symbol);
//		}
		
//		algorithmManager.pruneListOfSymbols(listOfString, exchange);
		algorithmManager.setListOfSymbols(multipleResultSetMarketScanner.listOfMultipleResultRowMarketScanner, exchange);
	}

	@Override
	public void failed(RequestHolder requestHolder) {

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

	@Override
	public void completed(MultipleResultSetMarketScanner multipleResultSetMarketScanner) {
		handleCompletedMarketScanner(multipleResultSetMarketScanner);
	}
}
