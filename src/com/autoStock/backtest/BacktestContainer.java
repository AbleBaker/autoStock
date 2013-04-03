package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Iterator;

import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestContainer implements ReceiverOfQuoteSlice {
	public Symbol symbol;
	private Exchange exchange;
	private HistoricalData historicalData;
	public AlgorithmTest algorithm;
	private ArrayList<DbStockHistoricalPrice> listOfDbHistoricalPrices = new ArrayList<DbStockHistoricalPrice>();
	private ListenerOfBacktestCompleted listener; 
	private Backtest backtest;
	private AlgorithmMode algorithmMode;
	public ArrayList<StrategyResponse> listOfStrategyResponse = new ArrayList<StrategyResponse>();
	
	public BacktestContainer(Symbol symbol, Exchange exchange, ListenerOfBacktestCompleted listener, AlgorithmMode algorithmMode){
		this.symbol = symbol;
		this.exchange = exchange;
		this.listener = listener;
		this.algorithmMode = algorithmMode;
	}
	
	@SuppressWarnings("deprecation")
	public void setBacktestData(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice, HistoricalData historicalData){
		this.listOfDbHistoricalPrices = listOfDbStockHistoricalPrice;
		this.historicalData = historicalData;
		
		Iterator<DbStockHistoricalPrice> iterator = this.listOfDbHistoricalPrices.iterator();
		
		while (iterator.hasNext()){
			DbStockHistoricalPrice dbStockHistoricalPrice = iterator.next();
			
			if (dbStockHistoricalPrice.dateTime.getHours() >= exchange.timeCloseForeign.hours && dbStockHistoricalPrice.dateTime.getMinutes() > exchange.timeCloseForeign.minutes){
				iterator.remove();
			}
		}
		
		algorithm = new AlgorithmTest(false, exchange, symbol, algorithmMode, historicalData.startDate);
		algorithm.init();
	}

	public void runBacktest(){
		if (listOfDbHistoricalPrices.size() == 0){
			endOfFeed(symbol);
		}
		backtest = new Backtest(historicalData, listOfDbHistoricalPrices, symbol);
		backtest.performBacktest(this);
	}
	
	public void setListener(ListenerOfBacktestCompleted listenerOfBacktestCompleted){
		this.listener = listenerOfBacktestCompleted;
	}
	
	public void reset(){
		listOfStrategyResponse.clear();
	}
	
	@Override
	public synchronized void receiveQuoteSlice(QuoteSlice quoteSlice) {
		algorithm.receiveQuoteSlice(quoteSlice);
	}
	
	@Override
	public synchronized void endOfFeed(Symbol symbol) {
		if (algorithmMode == AlgorithmMode.mode_backtest || algorithmMode == AlgorithmMode.mode_backtest_with_adjustment){
			listOfStrategyResponse.addAll(algorithm.listOfStrategyResponse);
		}
		algorithm.endOfFeed(symbol);
		listener.backtestCompleted(symbol, algorithm);
	}
}
