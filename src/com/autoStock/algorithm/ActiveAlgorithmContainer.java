package com.autoStock.algorithm;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.exchange.request.RequestMarketData;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionManager;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Period;
import com.autoStock.trading.types.Position;
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
	public Symbol symbol;
	private Exchange exchange;
	
	public ActiveAlgorithmContainer(boolean canTrade, Exchange exchange, Symbol symbol){
		this.symbol = symbol;
		this.exchange = exchange;
		algorithm = new AlgorithmTest(canTrade, exchange, symbol, AlgorithmMode.mode_engagement);
	}
	
	public void activate(){
		requestMarketData = new RequestMarketData(new RequestHolder(this), new RequestMarketDataListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				Co.println("--> Completed?");
			}
			
			@Override
			public void receiveQuoteSlice(RequestHolder requestHolder, QuoteSlice quoteSlice) {
				if (quoteSlice.priceClose != 0){
					algorithm.receiveQuoteSlice(quoteSlice);
				}
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetMarketData exResultSetMarketData) {
				Co.println("--> Completed?");
			}
		}, exchange, symbol, Period.min.duration * 1000);
	}
	
	public void deactivate(){
		Co.println("--> Deactivating: " + symbol.symbol);
		requestMarketData.cancel();
		algorithm.endOfFeed(symbol);
		Position position = PositionManager.instance.getPosition(symbol.symbol);
		if (position != null){
			if (position.positionType == PositionType.position_long){
				PositionManager.instance.suggestPosition(algorithm.currentQuoteSlice, algorithm.signal, PositionType.position_long_exit);
			}else if (position.positionType == PositionType.position_short){
				PositionManager.instance.suggestPosition(algorithm.currentQuoteSlice, algorithm.signal, PositionType.position_short_exit);	
			}else{
				throw new IllegalStateException();
			}
		}
		
		algorithm = null;
	}
}
