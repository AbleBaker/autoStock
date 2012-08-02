package com.autoStock.algorithm;

import com.autoStock.Co;
import com.autoStock.exchange.request.RequestMarketData;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Period;
import com.autoStock.trading.types.MarketData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class ActiveAlgorithmContainer {
	public AlgorithmTest algorithm;
	private RequestMarketData requestMarketData;
	private MarketData marketData;
	public Symbol symbol;
	public Exchange exchange;
	
	public ActiveAlgorithmContainer(boolean canTrade, Exchange exchange, Symbol symbol){
		this.symbol = symbol;
		this.exchange = exchange;
		algorithm = new AlgorithmTest(canTrade, exchange, symbol);
	}
	
	public void activate(){
		requestMarketData = new RequestMarketData(new RequestHolder(this), new RequestMarketDataListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				Co.println("--> Completed?");
			}
			
			@Override
			public void receiveQuoteSlice(RequestHolder requestHolder, QuoteSlice quoteSlice) {
				algorithm.receiveQuoteSlice(quoteSlice);
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetMarketData exResultSetMarketData) {
				Co.println("--> Completed?");
			}
		}, exchange, symbol, Period.min.duration * 1000);
	}
	
}
