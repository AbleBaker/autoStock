package com.autoStock;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.BacktestContainer;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.backtest.ListenerOfBacktestCompleted;
import com.autoStock.backtest.ListenerOfMainBacktestCompleted;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.Account;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.Global;
import com.autoStock.position.PositionManager;
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
	private BacktestType backtestType;
	private ArrayList<String> listOfStringBestBacktestResults = new ArrayList<String>();
	private ArrayList<HistoricalDataList> listOfHistoricalDataList = new ArrayList<HistoricalDataList>();
	private Exchange exchange;
	private int currentBacktestDayIndex = 0;
	private double metricBestAccountBalance = 0;
	private Benchmark bench = new Benchmark();
	private Lock lock = new Lock();
	private AtomicInteger callbacks = new AtomicInteger();
	private AlgorithmMode algorithmMode;
	private ArrayList<BacktestContainer> listOfBacktestContainer = new ArrayList<BacktestContainer>(0);
	private ListenerOfMainBacktestCompleted listenerOfMainBacktestCompleted;
	
	public MainBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols, BacktestType backtestType, ListenerOfMainBacktestCompleted listerListenerOfMainBacktestCompleted) {
		this.exchange = exchange;
		this.backtestType = backtestType;
		this.algorithmMode = AlgorithmMode.getFromBacktestType(backtestType);
		this.listenerOfMainBacktestCompleted = listerListenerOfMainBacktestCompleted;
		Global.callbackLock.requestLock();
		System.gc();
		
		if (algorithmMode.displayChart){
			Global.callbackLock.requestLock();
		}
		
		runMainBacktest(dateStart, dateEnd, listOfSymbols);
	}

	@SuppressWarnings("deprecation")
	public MainBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols, BacktestType backtestType) {
		this.exchange = exchange;
		this.backtestType = backtestType;
		this.algorithmMode = AlgorithmMode.getFromBacktestType(backtestType);
		Global.callbackLock.requestLock();
		System.gc();
		
		if (algorithmMode.displayChart){
			Global.callbackLock.requestLock();
		}
		
		runMainBacktest(dateStart, dateEnd, listOfSymbols);
	}
	
	private void runMainBacktest(Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols){
		Co.println("Main backtest...\n\n");
		
		adjustmentCampaign.prepare();
		
		HistoricalData baseHistoricalData = new HistoricalData(exchange, null, "STK", dateStart, dateEnd, Resolution.min);

		baseHistoricalData.startDate.setHours(exchange.timeOpenForeign.hours);
		baseHistoricalData.startDate.setMinutes(exchange.timeOpenForeign.minutes);
		baseHistoricalData.endDate.setHours(exchange.timeCloseForeign.hours);
		baseHistoricalData.endDate.setMinutes(exchange.timeCloseForeign.minutes);

		ArrayList<Date> listOfBacktestDates = DateTools.getListOfDatesOnWeekdays(baseHistoricalData.startDate, baseHistoricalData.endDate);
		
		if (listOfBacktestDates.size() == 0){
			throw new IllegalArgumentException();
		}
		
		for (Date date : listOfBacktestDates) {
			HistoricalDataList historicalDataList = new HistoricalDataList();
			for (String symbol : listOfSymbols){
				HistoricalData dayHistoricalData = new HistoricalData(exchange, new Symbol(symbol), baseHistoricalData.securityType, (Date)date.clone(), (Date)date.clone(), baseHistoricalData.resolution);
				dayHistoricalData.startDate.setHours(exchange.timeOpenForeign.hours);
				dayHistoricalData.endDate.setHours(exchange.timeCloseForeign.hours);
				historicalDataList.listOfHistoricalData.add(dayHistoricalData);
			}
			
			listOfHistoricalDataList.add(historicalDataList);
		}
		
		if (backtestType == BacktestType.backtest_adjustment){
			adjustmentCampaign.runAdjustment();
		}
		
		initBacktestContainers();
		runNextBacktestForDays();
	}
	
	private void initBacktestContainers(){
		HistoricalDataList historicalDataList = listOfHistoricalDataList.get(0);
		
		for (HistoricalData historicalData : historicalDataList.listOfHistoricalData){
			listOfBacktestContainer.add(new BacktestContainer(new Symbol(historicalData.symbol.symbolName), exchange, this, algorithmMode));
		}
	}
	
	private void runNextBacktestOnContainers(HistoricalDataList historicalDataList){
		
		callbacks.set(listOfBacktestContainer.size());
		
		Co.println("Backtesting (" + MathTools.round(adjustmentCampaign.getPercentComplete()*100) + "%): " + currentBacktestDayIndex);
		
		for (BacktestContainer backtestContainer : listOfBacktestContainer){
			HistoricalData historicalData = getHistoricalDataForSymbol(historicalDataList, backtestContainer.symbol.symbolName);
			ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, QueryArgs.symbol.setValue(historicalData.symbol.symbolName), QueryArgs.startDate.setValue(DateTools.getSqlDate(historicalData.startDate)), QueryArgs.endDate.setValue(DateTools.getSqlDate(historicalData.endDate)));

			if (listOfResults.size() > 0){	
				backtestContainer.setBacktestData(listOfResults, historicalData);
				backtestContainer.runBacktest();
			}
		}
	}
	
	private HistoricalData getHistoricalDataForSymbol(HistoricalDataList historicalDataList, String symbol){
		if (historicalDataList.listOfHistoricalData.size() == 0){
			throw new IllegalStateException("Historical data list size is 0 for symbol: " + symbol);
		}
		for (HistoricalData historicalData : historicalDataList.listOfHistoricalData){
			if (historicalData.symbol.symbolName.equals(symbol)){
				return historicalData;
			}
		}
		
		throw new IllegalStateException("No symbol data found for symbol: " + symbol);
	}
	
	public synchronized boolean runNextBacktestForDays(){
		if (currentBacktestDayIndex == listOfHistoricalDataList.size()){
			if (backtestType == BacktestType.backtest_default){
				Global.callbackLock.releaseLock();
				return false;
			}else if (backtestType == BacktestType.backtest_clustered_client){
				Global.callbackLock.releaseLock();
				if (listenerOfMainBacktestCompleted != null){
					listenerOfMainBacktestCompleted.backtestCompleted();
				}
				return false;
			}
			
			PositionManager.instance.executeSellAll();
			
			if (Account.instance.getAccountBalance() > metricBestAccountBalance){
				listOfStringBestBacktestResults.add(BacktestUtils.getCurrentBacktestValueGroup(listOfBacktestContainer.get(0).algorithm.strategy.signal));
				metricBestAccountBalance = Account.instance.getAccountBalance();
			}
						
			if (adjustmentCampaign.runAdjustment()) {
				currentBacktestDayIndex = 0;
				
				Account.instance.resetAccount();
				runNextBacktestForDays();
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
			Co.println("--> Backtest completed... " + symbol.symbolName + ", " + callbacks.get());
					
			if (callbacks.decrementAndGet() == 0){
				Co.println("--> All called back...\n");
				bench.printTick("Backtested");
				
				if (runNextBacktestForDays() == false){
					if (backtestType == BacktestType.backtest_default){
						Co.println(BacktestUtils.getCurrentBacktestValueGroup(listOfBacktestContainer.get(0).algorithm.strategy.signal));
					}
					Co.println("--> Finished backtest");
				}
			}
		}
	}
}
