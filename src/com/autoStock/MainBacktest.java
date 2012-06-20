package com.autoStock;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.backtest.Backtest;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.Account;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.CallbackLock;
import com.autoStock.internal.Global;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainBacktest implements ReceiverOfQuoteSlice {
	private AdjustmentCampaign adjustmentCampaign = AdjustmentCampaign.getInstance();
	private AlgorithmTest algorithm;
	private BacktestType backtestType = BacktestType.backtest_with_adjustment;
	private ArrayList<DbStockHistoricalPrice> listOfResults; 
	private ArrayList<String> listOfStringBestBacktestResults = new ArrayList<String>();
	private ArrayList<HistoricalData> listOfHistoricalData = new ArrayList<HistoricalData>();
	private Exchange exchange;
	private CallbackLock callbackLock = new CallbackLock();
	private int currentBacktestDayIndex = 0;
	private double metricBestAccountBalance = 0;
	private Benchmark bench = new Benchmark();

	@SuppressWarnings("deprecation")
	public MainBacktest(Exchange exchange, HistoricalData historicalData) {
		this.exchange = exchange;
		Global.callbackLock.requestLock();
		System.gc();
		
		Co.println("Main backtest...");

		historicalData.startDate.setHours(exchange.timeOpen.hour);
		historicalData.startDate.setMinutes(exchange.timeOpen.minute);
		historicalData.endDate.setHours(exchange.timeClose.hour);
		historicalData.endDate.setMinutes(exchange.timeClose.minute);

		ArrayList<Date> listOfBacktestDates = DateTools.getListOfDatesOnWeekdays(historicalData.startDate, historicalData.endDate);
		
		for (Date date : listOfBacktestDates) {
			HistoricalData dayHistoricalData = new HistoricalData(historicalData.symbol, historicalData.securityType, (Date)date.clone(), (Date)date.clone(), historicalData.resolution);
			dayHistoricalData.startDate.setHours(exchange.timeOpen.hour);
			dayHistoricalData.endDate.setHours(exchange.timeClose.hour);
			listOfHistoricalData.add(dayHistoricalData);
		}
		
		runNextBacktest();
	}
	
	public boolean runNextBacktest(){
		if (listOfHistoricalData.size() == currentBacktestDayIndex){
			if (backtestType == BacktestType.backtest_default){Global.callbackLock.releaseLock(); return false;}
			
			if (Account.instance.getBankBalance() > metricBestAccountBalance){
				listOfStringBestBacktestResults.add(BacktestUtils.getCurrentBacktestValueGroup());
				metricBestAccountBalance = Account.instance.getBankBalance();
			}
			
			Co.println("Account balance: " + Account.instance.getBankBalance());
			
			if (adjustmentCampaign.runAdjustment()) {
				currentBacktestDayIndex = 0;
				Account.instance.resetAccount();
				runNextBacktest();
			}else{
				Co.println("******** End of backtest and adjustment ********");
				BacktestUtils.printBestBacktestResults(listOfStringBestBacktestResults);
				Global.callbackLock.releaseLock();
			}
			
			return false;
		}else{
			HistoricalData historicalData = listOfHistoricalData.get(currentBacktestDayIndex++);
			
			Co.println("Backtesting: (" + MathTools.round(adjustmentCampaign.getPercentComplete()*100) + ") " + DateTools.getPrettyDate(historicalData.startDate) + " - " +  DateTools.getPrettyDate(historicalData.endDate));
			runBacktest(historicalData);
			
			return true;
		}
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

//		Co.println("******** End of feed in MainBacktest ********");

		if (backtestType == BacktestType.backtest_with_adjustment) {

			runNextBacktest();
		} else {
			Co.println("Algorithm has eneded: " + MathTools.round(Account.instance.getTransactionFeesPaid()) + ", " + Account.instance.getTransactions() + ", " + MathTools.round(Account.instance.getBankBalance()));
			runNextBacktest();
		}		
	}
}
