package com.autoStock;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.algorithm.AlgorithmBase;
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
import com.autoStock.order.OrderDefinitions.OrderMode;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseStatus;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.SignalMetric;
import com.autoStock.strategy.StrategyOfTest;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.Lock;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.HistoricalDataList;
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
		PositionManager.getInstance().orderMode = OrderMode.mode_simulated;
		
		if (algorithmMode.displayChart){
			Global.callbackLock.requestLock();
		}
		
		adjustmentCampaign.prepare();
		
		runMainBacktest(dateStart, dateEnd, listOfSymbols);
	}
	
	private void runMainBacktest(Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols){
		Co.println("Main backtest...\n\n");
		
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
		runNextBacktestForDays(false);
	}
	
	private void initBacktestContainers(){
		HistoricalDataList historicalDataList = listOfHistoricalDataList.get(0);
		
		for (HistoricalData historicalData : historicalDataList.listOfHistoricalData){
			listOfBacktestContainer.add(new BacktestContainer(new Symbol(historicalData.symbol.symbolName), exchange, this, algorithmMode));
		}
	}
	
	private void runNextBacktestOnContainers(HistoricalDataList historicalDataList){
		callbacks.set(listOfBacktestContainer.size());
		Co.println("Backtesting (" + MathTools.round(adjustmentCampaign.getPercentComplete()) + "%): " + currentBacktestDayIndex);
		
		boolean backtestContainedNoData = false;
		
		for (BacktestContainer backtestContainer : listOfBacktestContainer){
			HistoricalData historicalData = getHistoricalDataForSymbol(historicalDataList, backtestContainer.symbol.symbolName);
			ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, QueryArgs.symbol.setValue(historicalData.symbol.symbolName), QueryArgs.startDate.setValue(DateTools.getSqlDate(historicalData.startDate)), QueryArgs.endDate.setValue(DateTools.getSqlDate(historicalData.endDate)));

			if (listOfResults.size() > 0){	
				backtestContainer.setBacktestData(listOfResults, historicalData);
				backtestContainer.runBacktest();
			}else{
				Co.println("--> No backtest data! " + backtestContainer.symbol.symbolName);
				backtestContainedNoData = true;
				break;
			}
		}
		
		if (backtestContainedNoData){
			currentBacktestDayIndex++;
			runNextBacktestForDays(true);
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
	
	public synchronized boolean runNextBacktestForDays(boolean skippedDay){
		if (currentBacktestDayIndex == listOfHistoricalDataList.size()){
			if (backtestType == BacktestType.backtest_default){
				return false;
			}else if (backtestType == BacktestType.backtest_clustered_client){
				Global.callbackLock.releaseLock();
				if (listenerOfMainBacktestCompleted != null){
					listenerOfMainBacktestCompleted.backtestCompleted();
				}
				return false;
			}
			
			PositionManager.getInstance().executeExitAll();
			
			if (Account.getInstance().getAccountBalance() > metricBestAccountBalance){
				listOfStringBestBacktestResults.add(BacktestUtils.getCurrentBacktestValueGroup(listOfBacktestContainer.get(0).algorithm.strategy.signal));
				metricBestAccountBalance = Account.getInstance().getAccountBalance();
			}
						
			if (adjustmentCampaign.runAdjustment()) {
				currentBacktestDayIndex = 0;
				
				Account.getInstance().resetAccount();
				runNextBacktestForDays(false);
			}else{
				Co.println("******** End of backtest and adjustment ********");
				BacktestUtils.printBestBacktestResults(listOfStringBestBacktestResults);
				Global.callbackLock.releaseLock();
			}
			
			return false;
		}else{
			HistoricalDataList historicalDataList = listOfHistoricalDataList.get(currentBacktestDayIndex);
			runNextBacktestOnContainers(historicalDataList);
			if (skippedDay == false){currentBacktestDayIndex++;}
			return true;
		}
	}
	
	@Override
	public synchronized void backtestCompleted(Symbol symbol, AlgorithmBase algorithmBase) {
		Co.println("--> Backtest completed... " + symbol.symbolName + ", " + callbacks.get());

		if (callbacks.decrementAndGet() == 0) {
			Co.println("--> All called back...\n");
			bench.printTick("Backtested");

			if (runNextBacktestForDays(false) == false) {
				if (backtestType == BacktestType.backtest_default) {
					
					for (BacktestContainer backtestContainer : listOfBacktestContainer){
						Co.println("\n\n--> Backtest container: " + backtestContainer.symbol.symbolName);
						ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();

						for (StrategyResponse strategyResponse : backtestContainer.listOfStrategyResponse){
							ArrayList<String> listOfString = new ArrayList<String>();
							listOfString.add(DateTools.getPrettyDate(strategyResponse.quoteSlice.dateTime));
							listOfString.add(backtestContainer.symbol.symbolName);
							listOfString.add(String.valueOf(strategyResponse.quoteSlice.priceClose));
							listOfString.add(strategyResponse.strategyAction.name() + ", " + strategyResponse.strategyActionCause.name());
							listOfString.add(strategyResponse.positionGovernorResponse.status.name());
							
							String stringForSignal = new String();
							
							for (SignalMetric signalMetric : strategyResponse.signal.listOfSignalMetric){
								stringForSignal += signalMetric.signalMetricType.name() + ":" + signalMetric.strength + ", ";
							}
							
							listOfString.add(stringForSignal);
							
							if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit){
								listOfString.add("$ " + String.valueOf(strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission()));
							}else{
								listOfString.add("-");
							}
							
							listOfDisplayRows.add(listOfString);
						}
						
						new TableController().displayTable(AsciiTables.backtest_strategy_response, listOfDisplayRows);

					}
					
					Co.println(BacktestUtils.getCurrentBacktestCompleteValueGroup(listOfBacktestContainer.get(0).algorithm.strategy.signal, listOfBacktestContainer.get(0).algorithm.strategy.strategyOptions));
				} 
				Co.println("--> Finished backtest");
				Global.callbackLock.releaseLock();
			}
		}
	}
	
	public StrategyOfTest getStrategy(){
		return listOfBacktestContainer.get(0).algorithm.strategy;
	}
}
