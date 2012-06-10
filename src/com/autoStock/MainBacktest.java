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
	private BacktestType backtestType = BacktestType.backtest_default;
	
	private Permutation permutation = new Permutation();

	public MainBacktest(Exchange exchange, HistoricalData typeHistoricalData) {
		this.typeHistoricalData = typeHistoricalData;
		Global.callbackLock.requestCallbackLock();
		
		Co.println("Testing permutation...");
		
//		String[] arrayOfString = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
//		
//		PermutationCore gen = new PermutationCore(arrayOfString, 3);
//		ArrayList<String> v = (ArrayList<String>) gen.getVariations();
//		Collections.sort(v);
//		
//		for (String s : v) {
//		    System.out.println("--> " + s);
//		}
		
		permutation.addIteration(new Iteration(4, 7, AdjustmentDefinitions.algo_signal_sell));
		permutation.addIteration(new Iteration(8, 9, AdjustmentDefinitions.algo_signal_buy));
		permutation.addIteration(new Iteration(1, 3, AdjustmentDefinitions.algo_signal_short));
		
		permutation.prepare();
		
		while (permutation.iterate()){
			Co.println("Have permutation (sell): " + permutation.getIteration(AdjustmentDefinitions.algo_signal_sell).getCurrentValue());
			Co.println("Have permutation (buy): " + permutation.getIteration(AdjustmentDefinitions.algo_signal_buy).getCurrentValue());
			Co.println("Have permutation (short): " + permutation.getIteration(AdjustmentDefinitions.algo_signal_short).getCurrentValue());
			Co.println("");
		}
		
		int i=0;
		
//		while (permutation.iterate()){
//			Co.println("Iterated..." + i);
//			i++;
//		}


		// while
		// (adjustmentCampaign.runAdjustment(AdjustmentDefinitions.algo_signal_sell)){
		// Co.println("Adjustment: " +
		// adjustmentCampaign.getAdjustmentValueOfInt(AdjustmentDefinitions.algo_signal_sell));
		// }

		Global.callbackLock.releaseCallbackLock();

		// typeHistoricalData.startDate.setHours(exchange.timeOpen.hour);
		// typeHistoricalData.startDate.setMinutes(exchange.timeOpen.minute);
		// typeHistoricalData.endDate.setHours(exchange.timeClose.hour);
		// typeHistoricalData.endDate.setMinutes(exchange.timeClose.minute);
		// typeHistoricalData.symbol = "BAC";
		//
		// Co.println("Running backtest on Exchange: " + exchange.exchange);
		// Co.println("Running backtest for dates between " +
		// DateTools.getPrettyDate(typeHistoricalData.startDate) + " - " +
		// DateTools.getPrettyDate(typeHistoricalData.endDate));
		//
		// int days = typeHistoricalData.endDate.getDay() -
		// typeHistoricalData.startDate.getDay();
		//
		// runBacktest(typeHistoricalData);
	}

	@SuppressWarnings("unchecked")
	public void runBacktest(HistoricalData typeHistoricalData) {
		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, QueryArgs.symbol.setValue(typeHistoricalData.symbol), QueryArgs.startDate.setValue(DateTools.getSqlDate(typeHistoricalData.startDate)), QueryArgs.endDate.setValue(DateTools.getSqlDate(typeHistoricalData.endDate)));

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

		Co.println("End of feed in MainBacktest");

		if (backtestType == BacktestType.backtest_with_adjustment) {
			if (adjustmentCampaign.runAdjustment(AdjustmentDefinitions.analysis_macd_fast)) {
				Co.println("Algorithm has eneded : " + Account.instance.getTransactionFeesPaid());
				Account.instance.resetAccount();
				runBacktest(typeHistoricalData);
			}
		} else {
			Co.println("Algorithm has eneded : " + Account.instance.getBankBalance() + ", " + Account.instance.getTransactionFeesPaid());
		}

		Global.callbackLock.releaseCallbackLock();
	}
}
