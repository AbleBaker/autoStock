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
import com.autoStock.internal.ApplicationStates;
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
	private BacktestType backtestType;
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
	public MainBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols, BacktestType backtestType) {
		this.exchange = exchange;
		this.backtestType = backtestType;
		Global.callbackLock.requestLock();
		System.gc();
		
		Co.println("Main backtest...\n\n");
		
		HistoricalData baseHistoricalData = new HistoricalData(null, "STK", dateStart, dateEnd, Resolution.min);

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
				HistoricalData dayHistoricalData = new HistoricalData(symbol, baseHistoricalData.securityType, (Date)date.clone(), (Date)date.clone(), baseHistoricalData.resolution);
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
	
	public synchronized boolean runNextBacktestForDays(){
		if (currentBacktestDayIndex == listOfHistoricalDataList.size()){
			if (backtestType == BacktestType.backtest_default){Global.callbackLock.releaseLock(); return false;}
			
			PositionManager.instance.executeSellAll();
			
			if (Account.instance.getBankBalance() > metricBestAccountBalance){
				listOfStringBestBacktestResults.add(BacktestUtils.getCurrentBacktestValueGroup(listOfBacktestContainer.get(0).algorithm.signal));
				metricBestAccountBalance = Account.instance.getBankBalance();
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
			Position position = PositionManager.instance.getPosition(symbol.symbol);
			
			Co.println("--> Backtest completed... " + symbol.symbol + ", " + callbacks.get());
					
			if (callbacks.decrementAndGet() == 0){
				Co.println("--> All called back...\n\n");
//				bench.printTick("Backtested");
				
				if (runNextBacktestForDays() == false){
					if (backtestType == BacktestType.backtest_default){
						Co.println(BacktestUtils.getCurrentBacktestValueGroup(listOfBacktestContainer.get(0).algorithm.signal));
					}
					Co.println("--> Finished backtest");
				}
			}
		}
	}
}
