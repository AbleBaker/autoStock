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
import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.Signal;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.ShorlistOfStock;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class MarketScanner implements ReceiverOfQuoteSlice, AlgorithmListener {
	private ArrayList<ShorlistOfStock> listOfShortlistedStocks;
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
		for (ShorlistOfStock shortlistedStock : listOfShortlistedStocks){
			HistoricalData typeHistoricalData = new HistoricalData(shortlistedStock.symbol, "STK", DateTools.getDateFromString("2011-01-05 09:30:00"), DateTools.getDateFromString("2011-01-05 15:30:00"), Resolution.min);
			ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
					BasicQueries.basic_historical_price_range,
					QueryArgs.symbol.setValue(typeHistoricalData.symbol),
					QueryArgs.startDate.setValue(DateTools.getSqlDate(typeHistoricalData.startDate)),
					QueryArgs.endDate.setValue(DateTools.getSqlDate(typeHistoricalData.endDate)));
					
			Backtest backtest = new Backtest(typeHistoricalData, listOfResults, null);
		
			if (listOfResults.size() != 0){
				Co.println("Has size: " + listOfResults.size());
				listOfBacktest.add(backtest);
			}
		}
		
		Co.println("Initializing backtests... ");
		
		for (Backtest backtest : listOfBacktest){
			AlgorithmTest algorithmTest = new AlgorithmTest(false, null, null, null);
			algorithmTest.setAlgorithmListener(this);
			backtest.performBacktest(algorithmTest.getReceiver());
			listOfAlgorithmTest.add(algorithmTest);
		}
	}

	@Override
	public void receiveQuoteSlice(QuoteSlice quoteSlice) {
		Co.println("Received slice: " + quoteSlice.symbol);
	}

	@Override
	public void endOfFeed(Symbol symbol) {
		Co.println("End of feed");
	}

	@Override
	public void recieveSignal(Signal signal, QuoteSlice typeQuoteSlice) {
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
			PositionManager.instance.executeSellAll();
			Co.println("Account balance: " + Account.instance.getBankBalance() + " Fees paid: " + Account.instance.getTransactionFeesPaid());
		}
		
		endOfAlgorithmCount++;
	}

	@Override
	public void receivePositionGovernorResponse(PositionGovernorResponse positionGovernorResponse) {
	
	}
}
