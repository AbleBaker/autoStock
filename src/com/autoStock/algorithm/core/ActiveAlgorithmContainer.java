package com.autoStock.algorithm.core;

import java.util.Date;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.exchange.request.RequestMarketSymbolData;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketSymbolDataListener;
import com.autoStock.exchange.results.ExResultMarketSymbolData.ExResultSetMarketSymbolData;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionManager;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Period;
import com.autoStock.trading.types.MarketSymbolData;
import com.autoStock.trading.types.Position;
import com.autoStock.trading.yahoo.FundamentalData;
import com.autoStock.trading.yahoo.RequestFundamentalsListener;
import com.autoStock.trading.yahoo.YahooFundamentals;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class ActiveAlgorithmContainer {
	public final AlgorithmTest algorithm;
	public final Symbol symbol;
	public final Exchange exchange;
	public RequestMarketSymbolData requestMarketData;
	private YahooFundamentals yahooFundamentals;
	
	public ActiveAlgorithmContainer(boolean canTrade, Exchange exchange, Symbol symbol){
		this.symbol = symbol;
		this.exchange = exchange;
		algorithm = new AlgorithmTest(canTrade, exchange, symbol, AlgorithmMode.mode_engagement, new Date());
	}
	
	public void activate(){
		yahooFundamentals = new YahooFundamentals(new RequestFundamentalsListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				algorithm.disable("Failed to get fundamentals");
				return;
			}
			
			@Override
			public void success(FundamentalData fundamentalData) {
				if (fundamentalData.avgDailyVolume < 500000){
					algorithm.disable("Low volume");
					return;
				}
				
				algorithm.setFundamentalData(fundamentalData);
				startAlgorithmFeed();
			}
		}, exchange, symbol);
		
		yahooFundamentals.execute();
	}
	
	private void startAlgorithmFeed(){
		requestMarketData = new RequestMarketSymbolData(new RequestHolder(this), new RequestMarketSymbolDataListener() {
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
			public void completed(RequestHolder requestHolder, ExResultSetMarketSymbolData exResultSetMarketData) {
				Co.println("--> Completed?");
			}
		}, new MarketSymbolData(exchange, symbol), Period.min.seconds * 1000);
	}
	
	public void deactivate(){
		Co.println("--> Deactivating: " + symbol.symbolName);
		if (requestMarketData != null){requestMarketData.cancel();}
		if (yahooFundamentals != null){yahooFundamentals.cancel();}
		algorithm.endOfFeed(symbol);
		Position position = PositionManager.getInstance().getPosition(symbol);
		if (position != null){
			if (position.positionType == PositionType.position_long || position.positionType == PositionType.position_long_entry){
				PositionManager.getInstance().executePosition(algorithm.getCurrentQuoteSlice(), position.exchange, algorithm.strategy.signal, PositionType.position_long_exit, position, null);
			}else if (position.positionType == PositionType.position_short || position.positionType == PositionType.position_short_entry){
				PositionManager.getInstance().executePosition(algorithm.getCurrentQuoteSlice(), position.exchange, algorithm.strategy.signal, PositionType.position_short_exit, position, null);
			}else if (position.positionType == PositionType.position_failed){
				Co.println("--> Warning! Position status was failed while deactivating algorithm...");
			}else if (position.positionType == PositionType.position_cancelling || position.positionType == PositionType.position_cancelled){
				Co.println("--> Warning! Position status was cancelled while deactivating algorithm...");
			}else{
				throw new IllegalStateException();
			}
		}
	}
}
