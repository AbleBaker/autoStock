package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.algorithm.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
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
	private BacktestType backtestType;
	
	public BacktestContainer(Symbol symbol, Exchange exchange, ListenerOfBacktestCompleted listener, BacktestType backtestType){
		this.symbol = symbol;
		this.exchange = exchange;
		this.listener = listener;
		this.backtestType = backtestType;
	}
	
	public void setBacktestData(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice, HistoricalData historicalData){
		this.listOfDbHistoricalPrices = listOfDbStockHistoricalPrice;
		this.historicalData = historicalData;
		algorithm = new AlgorithmTest(false, exchange, symbol, AlgorithmMode.mode_backtest); //backtestType == BacktestType.backtest_default ? AlgorithmMode.mode_backtest : AlgorithmMode.mode_backtest_with_adjustment);
	}
	
	public synchronized void runBacktest(){
		backtest = new Backtest(historicalData, listOfDbHistoricalPrices, symbol);
		backtest.performBacktest(this);
	}
	
	@Override
	public synchronized void receiveQuoteSlice(QuoteSlice quoteSlice) {
		algorithm.receiveQuoteSlice(quoteSlice);
	}
	
	@Override
	public synchronized void endOfFeed(Symbol symbol) {
		algorithm.endOfFeed(symbol);
		listener.backtestCompleted(symbol);
	}
}
