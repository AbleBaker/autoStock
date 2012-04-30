/**
 * 
 */
package com.autoStock.scanner;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmListener;
import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.backtest.Backtest;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.Account;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.Signal;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalData.Resolution;
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.types.TypeQuoteSlice;
import com.autoStock.types.TypeShorlistStock;

/**
 * @author Kevin Kowalewski
 *
 */
public class MarketScanner implements ReceiverOfQuoteSlice, AlgorithmListener {
	private ArrayList<TypeShorlistStock> listOfShortlistedStocks;
	private ArrayList<Backtest> listOfBacktest = new ArrayList<Backtest>();
	private ArrayList<AlgorithmTest> listOfAlgorithmTest = new ArrayList<AlgorithmTest>();
	private Shortlist shortlist = new Shortlist("NYSE");
	private PositionManager positionManager = PositionManager.instance;
	private int endOfAlgorithmCount = 0;
	
	public MarketScanner(){
		Co.println("Getting shortlist");
		//listOfSymbolsFromDatabase = (ArrayList<DbSymbol>) new DatabaseQuery().getQueryResults(BasicQueries.basic_get_symbol_list_from_exchange, QueryArgs.exchange.setValue("NYSE"));
		listOfShortlistedStocks = shortlist.getShortlistedStocks();
	}
	
	public void startScan(){
		Co.println("Fetching symbol historical data...");
		for (TypeShorlistStock shortlistedStock : listOfShortlistedStocks){
			TypeHistoricalData typeHistoricalData = new TypeHistoricalData(shortlistedStock.symbol, "STK", DateTools.getDateFromString("2011-01-05 09:30:00"), DateTools.getDateFromString("2011-01-05 15:30:00"), Resolution.min);
			ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
					BasicQueries.basic_historical_price_range,
					QueryArgs.symbol.setValue(typeHistoricalData.symbol),
					QueryArgs.startDate.setValue(DateTools.getSqlDate(typeHistoricalData.startDate)),
					QueryArgs.endDate.setValue(DateTools.getSqlDate(typeHistoricalData.endDate)));
					
			Backtest backtest = new Backtest(typeHistoricalData, listOfResults);
		
			if (listOfResults.size() != 0){
				Co.println("Has size: " + listOfResults.size());
				listOfBacktest.add(backtest);
			}
		}
		
		Co.println("Initializing backtests... ");
		
		for (Backtest backtest : listOfBacktest){
			AlgorithmTest algorithmTest = new AlgorithmTest(false);
			algorithmTest.setAlgorithmListener(this);
			backtest.performBacktest(algorithmTest.getReceiver());
			listOfAlgorithmTest.add(algorithmTest);
		}
	}

	@Override
	public void receiveQuoteSlice(TypeQuoteSlice quoteSlice) {
		Co.println("Received slice: " + quoteSlice.symbol);
	}

	@Override
	public void endOfFeed() {
		Co.println("End of feed");
	}

	@Override
	public void recieveSignal(Signal signal, TypeQuoteSlice typeQuoteSlice) {
//		if (signal.getCombinedSignal() > 50){
//			//Co.println("Recieved buy signal: " + signal.getCombinedSignal());
//			positionManager.suggestPosition(typeQuoteSlice, signal);
//		}else if (signal.getCombinedSignal() < 50){
//			//Co.println("Recieved sell signal: " + signal.getCombinedSignal());
//			positionManager.suggestPosition(typeQuoteSlice, signal);
//		}
	}

	@Override
	public void endOfAlgorithm() {
		Co.println("End of algo: " + endOfAlgorithmCount + "," + listOfAlgorithmTest.size());
		if (endOfAlgorithmCount == listOfAlgorithmTest.size()-1){
			PositionManager.instance.induceSellAll();
			Co.println("Account balance: " + Account.instance.getBankBalance() + " Fees paid: " + Account.instance.getTransactionFeesPaid());
		}
		
		endOfAlgorithmCount++;
	}
}
