package com.autoStock;

import java.util.ArrayList;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentDefinitions;
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
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.types.TypeExchange;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainBacktest implements ReceiverOfQuoteSlice {
	private TypeHistoricalData typeHistoricalData;
	private ExResultHistoricalData exResultHistoricalData;
	private AdjustmentCampaign adjustmentCampaign = AdjustmentCampaign.getInstance();
	private AlgorithmTest algorithm;
	private ArrayList<Double> listOfAlorithmPerformance = new ArrayList<Double>();
	private BacktestType backtestType = BacktestType.backtest_default;
	
	public MainBacktest(TypeExchange exchange, TypeHistoricalData typeHistoricalData){
		this.typeHistoricalData = typeHistoricalData;
		Global.callbackLock.requestCallbackLock();
		
		typeHistoricalData.startDate.setHours(exchange.timeOpen.hour);
		typeHistoricalData.startDate.setMinutes(exchange.timeOpen.minute);
		typeHistoricalData.endDate.setHours(exchange.timeClose.hour);
		typeHistoricalData.endDate.setMinutes(exchange.timeClose.minute);
		typeHistoricalData.symbol = "BTU";
		
		Co.println("Running backtest on Exchange: " + exchange.exchange);
		Co.println("Running backtest for dates between " + DateTools.getPrettyDate(typeHistoricalData.startDate) + " - " + DateTools.getPrettyDate(typeHistoricalData.endDate));
		
		int days = typeHistoricalData.endDate.getDay() - typeHistoricalData.startDate.getDay();
		
		runBacktest(typeHistoricalData);
	}
	
	@SuppressWarnings("unchecked")
	public void runBacktest(TypeHistoricalData typeHistoricalData){
		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
				BasicQueries.basic_historical_price_range,
				QueryArgs.symbol.setValue(typeHistoricalData.symbol),
				QueryArgs.startDate.setValue(DateTools.getSqlDate(typeHistoricalData.startDate)),
				QueryArgs.endDate.setValue(DateTools.getSqlDate(typeHistoricalData.endDate)));

		Backtest backtest = new Backtest(typeHistoricalData, listOfResults);
		algorithm = new AlgorithmTest(false);
		backtest.performBacktest(this);
	}

	@Override
	public void receiveQuoteSlice(TypeQuoteSlice typeQuoteSlice) {
		algorithm.receiveQuoteSlice(typeQuoteSlice);
	}

	@Override
	public void endOfFeed() {
		algorithm.endOfFeed();
		
		Co.println("End of feed in MainBacktest");
	
		if (backtestType == BacktestType.backtest_with_adjustment){
			if (adjustmentCampaign.runAdjustment(AdjustmentDefinitions.analysis_macd_fast)){
				Co.println("Algorithm has eneded : " + Account.instance.getTransactionFeesPaid());
				Account.instance.resetAccount();
				runBacktest(typeHistoricalData);
			}
		}
		
		Global.callbackLock.releaseCallbackLock();
	}
}
