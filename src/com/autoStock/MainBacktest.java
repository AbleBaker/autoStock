package com.autoStock;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.backtest.BacktestContainer;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.backtest.ListenerOfBacktestCompleted;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.Account;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.Global;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalMetric;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.Lock;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.HistoricalDataList;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainBacktest implements ListenerOfBacktestCompleted {
	private AdjustmentCampaign adjustmentCampaign = AdjustmentCampaign.getInstance();
	private BacktestType backtestType = BacktestType.backtest_with_adjustment;
	private ArrayList<String> listOfStringBestBacktestResults = new ArrayList<String>();
	private ArrayList<HistoricalDataList> listOfHistoricalDataList = new ArrayList<HistoricalDataList>();
	private Exchange exchange;
	private int currentBacktestDayIndex = 0;
	private double metricBestAccountBalance = 0;
	private Benchmark bench = new Benchmark();
	private Lock lock = new Lock();
	private AtomicInteger callbacks = new AtomicInteger();
	
	private ArrayList<BacktestContainer> listOfBacktestContainer = new ArrayList<BacktestContainer>(0);

	@SuppressWarnings("deprecation")
	public MainBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols) {
		this.exchange = exchange;
		Global.callbackLock.requestLock();
		System.gc();
		
		Co.println("Main backtest...");
		
		HistoricalData baseHistoricalData = new HistoricalData(null, "STK", dateStart, dateEnd, Resolution.min);

		baseHistoricalData.startDate.setHours(exchange.timeOpen.hours);
		baseHistoricalData.startDate.setMinutes(exchange.timeOpen.minutes);
		baseHistoricalData.endDate.setHours(exchange.timeClose.hours);
		baseHistoricalData.endDate.setMinutes(exchange.timeClose.minutes);

		ArrayList<Date> listOfBacktestDates = DateTools.getListOfDatesOnWeekdays(baseHistoricalData.startDate, baseHistoricalData.endDate);
		
		if (listOfBacktestDates.size() == 0){
			throw new IllegalArgumentException();
		}
		
		for (Date date : listOfBacktestDates) {
			HistoricalDataList historicalDataList = new HistoricalDataList();
			for (String symbol : listOfSymbols){
				HistoricalData dayHistoricalData = new HistoricalData(symbol, baseHistoricalData.securityType, (Date)date.clone(), (Date)date.clone(), baseHistoricalData.resolution);
				dayHistoricalData.startDate.setHours(exchange.timeOpen.hours);
				dayHistoricalData.endDate.setHours(exchange.timeClose.hours);
				historicalDataList.listOfHistoricalData.add(dayHistoricalData);
			}
			
			listOfHistoricalDataList.add(historicalDataList);
		}
		
		initBacktestContainers();
		runNextBacktest();
	}
	
	private void initBacktestContainers(){
		HistoricalDataList historicalDataList = listOfHistoricalDataList.get(0);
		
		for (HistoricalData historicalData : historicalDataList.listOfHistoricalData){
			listOfBacktestContainer.add(new BacktestContainer(new Symbol(historicalData.symbol), exchange, this, backtestType));
		}
	}
	
	private void runNextBacktestOnContainers(HistoricalDataList historicalDataList){
		
		callbacks.set(listOfBacktestContainer.size());
		
		Co.println("Backtesting (" + MathTools.round(adjustmentCampaign.getPercentComplete()*100) + "%): " + currentBacktestDayIndex);
		
		for (BacktestContainer backtestContainer : listOfBacktestContainer){
			HistoricalData historicalData = getHistoricalDataForSymbol(historicalDataList, backtestContainer.symbol.symbol);
			ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, QueryArgs.symbol.setValue(historicalData.symbol), QueryArgs.startDate.setValue(DateTools.getSqlDate(historicalData.startDate)), QueryArgs.endDate.setValue(DateTools.getSqlDate(historicalData.endDate)));

			if (listOfResults.size() > 0){	
				backtestContainer.setBacktestData(listOfResults, historicalData);
				backtestContainer.runBacktest();
			}
		}
	}
	
	private HistoricalData getHistoricalDataForSymbol(HistoricalDataList historicalDataList, String symbol){
		for (HistoricalData historicalData : historicalDataList.listOfHistoricalData){
			if (historicalData.symbol.equals(symbol)){
				return historicalData;
			}
		}
		
		throw new IllegalStateException();
	}
	
	public boolean runNextBacktest(){
		if (listOfHistoricalDataList.size() == currentBacktestDayIndex){
			if (backtestType == BacktestType.backtest_default){Global.callbackLock.releaseLock(); return false;}
						
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
			HistoricalDataList historicalDataList = listOfHistoricalDataList.get(currentBacktestDayIndex);
			runNextBacktestOnContainers(historicalDataList);
			currentBacktestDayIndex++;
			return true;
		}
	}
	
	@Override
	public synchronized void backtestCompleted(Symbol symbol) {	
		synchronized (lock){
			Position position = PositionManager.instance.getPosition(symbol.symbol);
			
			Co.println("--> Backtest completed... " + symbol.symbol + ", " + callbacks.get());
					
			if (callbacks.decrementAndGet() == 0){
				Co.println("--> All called back...");
				
				PositionManager.instance.executeSellAll();
				
				if (Account.instance.getBankBalance() > metricBestAccountBalance){
					Signal signal = new Signal(SignalSource.from_manual);
					signal.addSignalMetrics(new SignalMetric(0, SignalMetricType.metric_rsi));
					listOfStringBestBacktestResults.add(BacktestUtils.getCurrentBacktestValueGroup(signal));
					metricBestAccountBalance = Account.instance.getBankBalance();
				}
	
				Co.println("Account balance: " + Account.instance.getBankBalance() + ", " + Account.instance.getTransactions() + MathTools.round(Account.instance.getTransactionFeesPaid()) + "\n\n");
				
				if (runNextBacktest() == false){
					Co.println("--> Finished backtest");
				}
			}
		}
	}
}
