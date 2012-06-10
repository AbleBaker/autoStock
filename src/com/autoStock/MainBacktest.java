package com.autoStock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.Permutation;
import com.autoStock.adjust.PermutationCore;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentDefinitions;
import com.autoStock.adjust.Permutation.Iteration;
import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.backtest.Backtest;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.exchange.results.ExResultHistoricalData;
import com.autoStock.finance.Account;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.Global;
import com.autoStock.signal.SignalControl;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainBacktest implements ReceiverOfQuoteSlice {
	private HistoricalData typeHistoricalData;
	private ExResultHistoricalData exResultHistoricalData;
	private AdjustmentCampaign adjustmentCampaign = AdjustmentCampaign.getInstance();
	private AlgorithmTest algorithm;
	private ArrayList<Double> listOfAlorithmPerformance = new ArrayList<Double>();
	private BacktestType backtestType = BacktestType.backtest_with_adjustment;
	private ArrayList<DbStockHistoricalPrice> listOfResults; 
	private Benchmark bench;
	private ArrayList<String> listOfStringBestBacktestResults = new ArrayList<String>();
	
	private double metricBestAccountBalance = 0;

	public MainBacktest(Exchange exchange, HistoricalData typeHistoricalData) {
		this.typeHistoricalData = typeHistoricalData;
		Global.callbackLock.requestCallbackLock();
		
		Co.println("Testing permutation...");

		 typeHistoricalData.startDate.setHours(exchange.timeOpen.hour);
		 typeHistoricalData.startDate.setMinutes(exchange.timeOpen.minute);
		 typeHistoricalData.endDate.setHours(exchange.timeClose.hour);
		 typeHistoricalData.endDate.setMinutes(exchange.timeClose.minute);
		 typeHistoricalData.symbol = "BAC";
		
		 Co.println("Running backtest on Exchange: " + exchange.exchange);
		 Co.println("Running backtest for dates between " +
		 DateTools.getPrettyDate(typeHistoricalData.startDate) + " - " +
		 DateTools.getPrettyDate(typeHistoricalData.endDate));
		
		 int days = typeHistoricalData.endDate.getDay() -
		 typeHistoricalData.startDate.getDay();
		
		 runBacktest(typeHistoricalData);
	}

	@SuppressWarnings("unchecked")
	public void runBacktest(HistoricalData typeHistoricalData) {
		bench = new Benchmark();
		if (listOfResults == null){
			listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, QueryArgs.symbol.setValue(typeHistoricalData.symbol), QueryArgs.startDate.setValue(DateTools.getSqlDate(typeHistoricalData.startDate)), QueryArgs.endDate.setValue(DateTools.getSqlDate(typeHistoricalData.endDate)));
		}

		Backtest backtest = new Backtest(typeHistoricalData, listOfResults);
		algorithm = new AlgorithmTest(false);
		backtest.performBacktest(this);
	}

	@Override
	public void receiveQuoteSlice(QuoteSlice typeQuoteSlice) {
		algorithm.receiveQuoteSlice(typeQuoteSlice);
	}

	@Override
	public void endOfFeed() {
		algorithm.endOfFeed();

		Co.println("******** End of feed in MainBacktest ********");

		if (backtestType == BacktestType.backtest_with_adjustment) {
			Co.println("Algorithm has eneded 1: " + Account.instance.getTransactionFeesPaid() + ", " + Account.instance.getBankBalance());
			bench.printTotal();
			Co.println("\n\n");
			
			if (Account.instance.getBankBalance() > metricBestAccountBalance){
				listOfStringBestBacktestResults.add("88888888 Better backtest result: " + 
					SignalControl.pointToSignalLongEntry + ", " + 
					SignalControl.pointToSignalLongExit + ", " + 
					SignalControl.pointToSignalShortEntry + ", " +
					SignalControl.pointToSignalShortExit + ", ");
				
				metricBestAccountBalance = Account.instance.getBankBalance();
			}

			if (adjustmentCampaign.runAdjustment()) {
				Account.instance.resetAccount();
				runBacktest(typeHistoricalData);
			}else{
				Co.println("Best backtest results...");
				for (String string : listOfStringBestBacktestResults){
					Co.println(string);
				}
				bench.printTotal();
				Global.callbackLock.releaseCallbackLock();
			}
		} else {
			Co.println("Algorithm has eneded 2: " + Account.instance.getBankBalance() + ", " + Account.instance.getTransactionFeesPaid());
			Global.callbackLock.releaseCallbackLock();
		}		
	}
}
