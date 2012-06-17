package com.autoStock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.autoStock.internal.CallbackLock;
import com.autoStock.internal.Global;
import com.autoStock.signal.SignalControl;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainBacktest implements ReceiverOfQuoteSlice {
	private HistoricalData historicalDataAtCurrent;
	private AdjustmentCampaign adjustmentCampaign = AdjustmentCampaign.getInstance();
	private AlgorithmTest algorithm;
	private BacktestType backtestType = BacktestType.backtest_default;
	private ArrayList<DbStockHistoricalPrice> listOfResults; 
	private ArrayList<String> listOfStringBestBacktestResults = new ArrayList<String>();
	private ArrayList<Date> listOfBacktestDates;
	private Exchange exchange;
	private CallbackLock callbackLock = new CallbackLock();
	
	private double metricBestAccountBalance = 0;

	public MainBacktest(Exchange exchange, HistoricalData historicalData) {
		this.exchange = exchange;
		Global.callbackLock.requestLock();
		System.gc();
		
		Co.println("Main backtest...");

		historicalData.startDate.setHours(exchange.timeOpen.hour);
		historicalData.startDate.setMinutes(exchange.timeOpen.minute);
		historicalData.endDate.setHours(exchange.timeClose.hour);
		historicalData.endDate.setMinutes(exchange.timeClose.minute);
		historicalData.symbol = "NOK";

		listOfBacktestDates = DateTools.getListOfDatesOnWeekdays(historicalData.startDate, historicalData.endDate);

		for (Date date : listOfBacktestDates) {
			callbackLock.requestLock();
			HistoricalData dayHistoricalData = new HistoricalData(historicalData.symbol, historicalData.securityType, (Date)date.clone(), (Date)date.clone(), historicalData.resolution);
			dayHistoricalData.startDate.setHours(exchange.timeOpen.hour);
			dayHistoricalData.endDate.setHours(exchange.timeClose.hour);
			
			historicalDataAtCurrent = dayHistoricalData;
			Co.println("Running backtest on Exchange: " + exchange.name + " for dates between " + DateTools.getPrettyDate(dayHistoricalData.startDate) + " - " + DateTools.getPrettyDate(dayHistoricalData.endDate));
			runBacktest(dayHistoricalData);
			
			while (callbackLock.isLocked()){
				try {Thread.sleep(1);}catch(InterruptedException e){return;}
			}
		}
		
		Global.callbackLock.releaseLock();
	}

	@SuppressWarnings("unchecked")
	public void runBacktest(HistoricalData historicalData) {
		listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, QueryArgs.symbol.setValue(historicalData.symbol), QueryArgs.startDate.setValue(DateTools.getSqlDate(historicalData.startDate)), QueryArgs.endDate.setValue(DateTools.getSqlDate(historicalData.endDate)));

		if (listOfResults.size() > 0){
			Backtest backtest = new Backtest(historicalData, listOfResults);
			algorithm = new AlgorithmTest(false, exchange);
			backtest.performBacktest(this);
		}else{
			Co.println("Warning, no data for weekday..." + DateTools.getPrettyDate(historicalData.startDate));
			callbackLock.releaseLock();
		}
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
			Co.println("Algorithm has eneded 1: " + MathTools.round(Account.instance.getTransactionFeesPaid()) + ", " + Account.instance.getTransactions() + ", " + MathTools.round(Account.instance.getBankBalance()));
			Co.println("\n\n");
			
			if (Account.instance.getBankBalance() > metricBestAccountBalance){
				listOfStringBestBacktestResults.add("--------> Best backtest results: " + 
					SignalControl.pointToSignalLongEntry + "\n" + 
					SignalControl.pointToSignalLongExit + "\n" + 
					SignalControl.pointToSignalShortEntry + "\n" +
					SignalControl.pointToSignalShortExit + "\n" +
					SignalControl.periodLength + "\n" + 
					SignalControl.periodWindow + "\n" + 
					SignalControl.periodAverageForPPC + "\n" + 
					SignalControl.periodAverageForDI + "\n" + 
					SignalControl.periodAverageForCCI + "\n" + 
					SignalControl.periodAverageForMACD + "\n" + 
					SignalControl.periodAverageForTRIX + "\n" +
					SignalControl.weightForPPC + "\n" +
					SignalControl.weightForDI + "\n" + 
					SignalControl.weightForCCI + "\n" + 
					SignalControl.weightForMACD + "\n" + 
					SignalControl.weightForTRIX + "\n" +
					Account.instance.getTransactions() + "\n" + 
					Account.instance.getBankBalance() + "\n");
				
				metricBestAccountBalance = Account.instance.getBankBalance();
			}

			if (adjustmentCampaign.runAdjustment()) {
				Account.instance.resetAccount();
				runBacktest(historicalDataAtCurrent);
			}else{
				Co.println("Best backtest results...");
				for (String string : listOfStringBestBacktestResults){
					Co.println(string);
				}
				Global.callbackLock.releaseLock();
			}
		} else {
			Co.println("Algorithm has eneded 2: " + Account.instance.getBankBalance() + ", " + Account.instance.getTransactionFeesPaid());
			callbackLock.releaseLock();
		}		
	}
}
