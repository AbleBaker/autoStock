package com.autoStock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.encog.neural.networks.BasicNetwork;

import com.autoStock.account.AccountProvider;
import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentCampaignProvider;
import com.autoStock.adjust.AdjustmentIdentifier;
import com.autoStock.adjust.AdjustmentRebaser;
import com.autoStock.adjust.AdjustmentSeriesForAlgorithm;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.core.AlgorithmRemodeler;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestContainer;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.BacktestEvaluationWriter;
import com.autoStock.backtest.BacktestEvaluator;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.backtest.BacktestUtils.BacktestResultTransactionDetails;
import com.autoStock.backtest.ListenerOfBacktest;
import com.autoStock.backtest.ListenerOfMainBacktestCompleted;
import com.autoStock.cache.GenericPersister;
import com.autoStock.chart.CombinedLineChart.StoredSignalPoint;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.Global;
import com.autoStock.order.OrderDefinitions.OrderMode;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.extras.EncogNetworkProvider;
import com.autoStock.strategy.StrategyBase;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.tools.TimeToCompletionEstimate;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.HistoricalDataList;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainBacktest implements ListenerOfBacktest {
	private boolean USE_AUTOGENERATED_NETWORK = true;
	private final boolean USE_CLICKPOINTS = false;
	private AdjustmentCampaignProvider adjustmentCampaignProvider = AdjustmentCampaignProvider.getInstance();
	private BacktestType backtestType;
	private ArrayList<HistoricalDataList> listOfHistoricalDataList = new ArrayList<HistoricalDataList>();
	private Exchange exchange;
	private int currentBacktestDayIndex = 0;
	private int currentBacktestComputeUnitIndex = 0;
	private double metricBestAccountBalance = 0;
	private Benchmark bench = new Benchmark();
	private AtomicInteger callbacks = new AtomicInteger();
	private AlgorithmMode algorithmMode;
	private ArrayList<BacktestContainer> listOfBacktestContainer = new ArrayList<BacktestContainer>();
	private ListenerOfMainBacktestCompleted listenerOfMainBacktestCompleted;
	private ArrayList<String> listOfStringBestBacktestResults = new ArrayList<String>();
	public final BacktestEvaluator backtestEvaluator = new BacktestEvaluator();
	private HashMap<Symbol, ArrayList<AlgorithmModel>> hashOfAlgorithmModel;
	private TimeToCompletionEstimate eta = new TimeToCompletionEstimate();

	public MainBacktest(Exchange exchange, Date dateStart, Date dateEnd, BacktestType backtestType, ListenerOfMainBacktestCompleted listerListenerOfMainBacktestCompleted, HashMap<Symbol, ArrayList<AlgorithmModel>> hashOfAlgorithmModel) {
		this.exchange = exchange;
		this.backtestType = backtestType;
		this.algorithmMode = AlgorithmMode.getFromBacktestType(backtestType);
		this.listenerOfMainBacktestCompleted = listerListenerOfMainBacktestCompleted;
		this.hashOfAlgorithmModel = hashOfAlgorithmModel;
		Global.callbackLock.requestLock();

		if (algorithmMode.displayChart) {
			Global.callbackLock.requestLock();
		}

		startMainBacktest(dateStart, dateEnd, new ArrayList<Symbol>(hashOfAlgorithmModel.keySet()));
	}

	@SuppressWarnings("deprecation")
	public MainBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<Symbol> listOfSymbols, BacktestType backtestType) {
		this.exchange = exchange;
		this.backtestType = backtestType;
		this.algorithmMode = AlgorithmMode.getFromBacktestType(backtestType);
		Global.callbackLock.requestLock();
		PositionManager.getGlobalInstance().orderMode = OrderMode.mode_simulated;

		if (algorithmMode.displayChart) {
			Global.callbackLock.requestLock();
		}

		startMainBacktest(dateStart, dateEnd, listOfSymbols);
	}

	private void startMainBacktest(Date dateStart, Date dateEnd, ArrayList<Symbol> listOfSymbols) {
		if (backtestType != BacktestType.backtest_result_only) {
			Co.println("Main backtest...\n\n");
		}
		
		if (listOfSymbols.size() == 0){
			throw new IllegalArgumentException("No symbols specified");
		}
		
		if (backtestType == BacktestType.backtest_bgi){USE_AUTOGENERATED_NETWORK = true;}
		
		ListTools.removeDuplicates(listOfSymbols);
		listOfHistoricalDataList = BacktestUtils.getHistoricalDataList(exchange, dateStart, dateEnd, listOfSymbols);
		
		setupBacktestContainersAndAdjustment();

		if (backtestType == BacktestType.backtest_adjustment_boilerplate) {
			adjustmentCampaignProvider.applyBoilerplateValues();
		} else if (backtestType == BacktestType.backtest_adjustment_individual) {
			for (Pair<AdjustmentIdentifier, AdjustmentCampaign> pair : adjustmentCampaignProvider.getListOfAdjustmentCampaign()) {
				pair.second.applyValues();
			}
		}else if (backtestType == BacktestType.backtest_clustered_client){
			//pass
		}
		
		runNextBacktestForDays(false);
	}

	private void setupBacktestContainersAndAdjustment() {
		HistoricalDataList historicalDataList = listOfHistoricalDataList.get(0);

		for (HistoricalData historicalData : historicalDataList.listOfHistoricalData) {
			BacktestContainer backtestContainer = new BacktestContainer(historicalData.symbol, exchange, this, algorithmMode);
			listOfBacktestContainer.add(backtestContainer);

			if (backtestType == BacktestType.backtest_adjustment_individual){
				AdjustmentCampaign adjustmentCampaign = new AdjustmentSeriesForAlgorithm(backtestContainer.algorithm);
				adjustmentCampaign.initialize();
				adjustmentCampaignProvider.addAdjustmentCampaignForAlgorithm(adjustmentCampaign, historicalData.symbol);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void runNextBacktestOnContainers(HistoricalDataList historicalDataList) {
		callbacks.set(listOfBacktestContainer.size());
		if (backtestType != BacktestType.backtest_result_only) {
			Co.println("\nBacktesting " + historicalDataList.listOfHistoricalData.get(0).startDate);
		}

		boolean backtestContainedNoData = false;

		PositionManager.getGlobalInstance().reset();

		for (BacktestContainer backtestContainer : listOfBacktestContainer) {
			if (backtestContainer.isIncomplete()) {
				HistoricalData historicalData = BacktestUtils.getHistoricalDataForSymbol(historicalDataList, backtestContainer.symbol.name);
				ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.name), new QueryArg(QueryArgs.exchange, historicalData.exchange.name), new QueryArg(QueryArgs.resolution, historicalData.resolution.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));

				if (listOfResults.size() > 0) {
					backtestContainer.setBacktestData(listOfResults, historicalData);

					// Use autogen network
					if (USE_AUTOGENERATED_NETWORK){
						Co.println("--> Using auto generated network");
						BasicNetwork basicNetwork = new EncogNetworkProvider().getBasicNetwork(backtestContainer.exchange.name + "-" + backtestContainer.symbol.name + "-day-" + DateTools.getEncogDate(historicalData.startDate));
						if (basicNetwork == null){throw new IllegalStateException("Couldn't find network to load");}
						backtestContainer.algorithm.signalGroup.signalOfEncog.setNetwork(basicNetwork, 0);
					}
					
					if (USE_CLICKPOINTS && GenericPersister.getStaticInstance().getCount(StoredSignalPoint.class) > 0){
						backtestContainer.algorithm.positionGovernor.listOfPredSignalPoint = BacktestUtils.getListOfChartSignalPoints(backtestContainer.symbol, backtestContainer.exchange, historicalData.startDate, historicalData.endDate); 
					}
					
					backtestContainer.runBacktest();
				} else {
					Co.println("--> No backtest data! " + backtestContainer.symbol.name);
					backtestContainedNoData = true;
					break;
				}
			} else {
				Co.println("--> Adjustment is already complete for: " + backtestContainer.symbol.name);
				callbacks.decrementAndGet();
			}
		}

		if (backtestContainedNoData) {
			currentBacktestDayIndex++;
			
			if (runNextBacktestForDays(true) == false){
				endOfBacktests();
			}
		}
	}

	private synchronized boolean runNextBacktestForDays(boolean skippedDay) {
		if (backtestType == BacktestType.backtest_adjustment_boilerplate){
			throw new UnknownError("Not sure how to handle this becuase I can't rebase");
		}else if (backtestType == BacktestType.backtest_adjustment_individual){
			for (Pair<AdjustmentIdentifier, AdjustmentCampaign> pair : adjustmentCampaignProvider.getListOfAdjustmentCampaign()) {
				if (pair.second.rebaseRequired()){
					pair.second.isRebasing = true;
				}
			}
		}
				
		if (currentBacktestDayIndex == listOfHistoricalDataList.size()) {
			
			if (backtestType == BacktestType.backtest_default || backtestType == BacktestType.backtest_result_only) {
				return false;
			} else if (backtestType == BacktestType.backtest_clustered_client) {
				int maxUnitSize = 0;
				
				for (Symbol symbol : hashOfAlgorithmModel.keySet()){
					maxUnitSize = Math.max(maxUnitSize, hashOfAlgorithmModel.get(symbol).size());
				}
				
				for (BacktestContainer backtestContainer : listOfBacktestContainer) {
					BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(backtestContainer);
					backtestEvaluator.addResult(backtestContainer.symbol, backtestEvaluation, false);
					
					backtestContainer.reset();
				}
								
				if (currentBacktestComputeUnitIndex < maxUnitSize-1){		
					currentBacktestDayIndex = 0;
					currentBacktestComputeUnitIndex++;
					
					return runNextBacktestForDays(false);
				}else{
					Co.println("--> No iterations left");
					return false;
				}
			}

			if (backtestType == BacktestType.backtest_adjustment_boilerplate) {
				if (AccountProvider.getInstance().getGlobalAccount().getBalance() > metricBestAccountBalance) {
					if (listOfBacktestContainer.get(0).algorithm != null) {
						BacktestResultTransactionDetails backtestDetails = BacktestUtils.getProfitLossDetails(listOfBacktestContainer);
						listOfStringBestBacktestResults.add(BacktestUtils.getCurrentBacktestCompleteValueGroup(listOfBacktestContainer.get(0).algorithm.strategyBase.signaler, listOfBacktestContainer.get(0).algorithm.strategyBase.strategyOptions, backtestDetails, backtestType, AccountProvider.getInstance().getGlobalAccount()));
					}
					metricBestAccountBalance = AccountProvider.getInstance().getGlobalAccount().getBalance();
				}
				
				if (adjustmentCampaignProvider.runBoilerplateAdjustment()) {
					currentBacktestDayIndex = 0;
					AccountProvider.getInstance().getGlobalAccount().reset();

					for (BacktestContainer backtestContainer : listOfBacktestContainer) {
						backtestContainer.reset();
					}

					return runNextBacktestForDays(false);
				} else {
					Co.println("******** End of backtest and adjustment ********");
					BacktestUtils.printBestBacktestResults(listOfStringBestBacktestResults);
					Global.callbackLock.releaseLock();
				}
			} else if (backtestType == BacktestType.backtest_adjustment_individual) {
				Co.println("--> Finished all days: " + adjustmentCampaignProvider.isRebasingIndividual());

				for (Pair<AdjustmentIdentifier, AdjustmentCampaign> pair : adjustmentCampaignProvider.getListOfAdjustmentCampaign()) {
					eta.setMaxIndex(pair.second.getMaxIndex());
					eta.update(pair.second.getCurrentIndex());
					
					pair.second.printPercentComplete(pair.first.identifier.name + " - " + (int) eta.getETAInMinutes() + " minutes");
				}
				
				if (adjustmentCampaignProvider.isRebasingIndividual()){
					Co.println("--> Is rebased?");
					
					for (Pair<AdjustmentIdentifier, AdjustmentCampaign> pair : adjustmentCampaignProvider.getListOfAdjustmentCampaign()) {
						if (pair.second.isRebasing){
							Co.println("--> Need to rebase: " + pair.first.identifier.name);
							
							BacktestContainer backtestContainer = BacktestUtils.getBacktestContainerForSymbol(pair.first.identifier, listOfBacktestContainer);
							
							new AdjustmentRebaser(pair.second, backtestContainer).rebase();
							backtestContainer.reset();
							
							pair.second.applyValues();
							pair.second.isRebasing = false;
						}
					}
					
					Co.println("--> Was rebased! - Re-run current iteration!");
					
					currentBacktestDayIndex = 0;
					return runNextBacktestForDays(false);
				}
								
				for (BacktestContainer backtestContainer : listOfBacktestContainer) {
					BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(backtestContainer);
					backtestEvaluator.addResult(backtestContainer.symbol, backtestEvaluation, true);
				}
				
				if (runAdustmentSeries()) {
					currentBacktestDayIndex = 0;
					AccountProvider.getInstance().getGlobalAccount().reset();

					for (BacktestContainer backtestContainer : listOfBacktestContainer) {
						backtestContainer.reset();
					}

					return runNextBacktestForDays(false);
				} else {
					Co.println("******** End of backtest and adjustment ********");
					
					backtestEvaluator.pruneForFinish();			
					backtestEvaluator.reverse();
					
					for (BacktestContainer backtestContainer : listOfBacktestContainer) {
						Co.println("--> SYMBOL BACKTEST: " + backtestContainer.symbol.name);
						ArrayList<BacktestEvaluation> listOfBacktestEvaluation = backtestEvaluator.getResults(backtestContainer.symbol); 
						if (listOfBacktestEvaluation == null){
							Co.println("--> No results");
						}else{
							for (BacktestEvaluation backtestEvaluation : listOfBacktestEvaluation){
								Co.println("\n\n--> String representation: " + backtestEvaluation.toString());
							}
							
							BacktestEvaluation bestEvaluation = backtestEvaluator.getResults(backtestContainer.symbol).get(backtestEvaluator.getResults(backtestContainer.symbol).size() -1);
							new BacktestEvaluationWriter().writeToDatabase(bestEvaluation, true);
						}
					}
					
					BacktestUtils.printBestBacktestResults(listOfStringBestBacktestResults);
					Global.callbackLock.releaseLock();
				}
			}
			return false;
		} else {
			Co.println("--> Run on day");

			if (backtestType == BacktestType.backtest_clustered_client){
				remodelBacktestContainers();
			}
			
			HistoricalDataList historicalDataList = listOfHistoricalDataList.get(currentBacktestDayIndex);
			runNextBacktestOnContainers(historicalDataList);
			if (skippedDay == false) {
				currentBacktestDayIndex++;
			}
			return true;
		}
	}
	
	private void remodelBacktestContainers(){
		for (BacktestContainer backtestContainer : listOfBacktestContainer) {
//			Co.println("--> About to remodel: " + currentBacktestComputeUnitIndex);
			new AlgorithmRemodeler(backtestContainer.algorithm, hashOfAlgorithmModel.get(backtestContainer.symbol).get(currentBacktestComputeUnitIndex)).remodel();
		}
	}

	private boolean runAdustmentSeries() {
		boolean adjustmentRun = false;

		for (Pair<AdjustmentIdentifier, AdjustmentCampaign> pair : adjustmentCampaignProvider.getListOfAdjustmentCampaign()) {
			if (pair.second.hasMore()) {
				if (pair.second.rebaseRequired()){
					throw new IllegalStateException("Need to rebase!");
				}
				pair.second.runAdjustment();
				adjustmentRun = true;
			} else {
				for (BacktestContainer backtestContainer : listOfBacktestContainer) {
					if (backtestContainer.symbol.name.equals(pair.first.identifier.name)) {
						// Co.println("--> Algorithm marked as complete: " + backtestContainer.symbol.symbolName);
						backtestContainer.markAsComplete();
					}
				}
			}
		}

		return adjustmentRun;
	}

	@Override
	public synchronized void onCompleted(Symbol symbol, AlgorithmBase algorithmBase) {
		if (backtestType != BacktestType.backtest_result_only) {
			Co.print("[ " + symbol.name + " ] ");
		}

		if (callbacks.decrementAndGet() == 0) {
			endOfBacktests();
		}
	}
	
	private void endOfBacktests(){
		if (backtestType != BacktestType.backtest_result_only) {
			bench.printTick("Backtested all symbols");
		}

		PositionManager.getGlobalInstance().executeExitAll();

		if (runNextBacktestForDays(false) == false) {
			if (backtestType == BacktestType.backtest_default) {
				for (BacktestContainer backtestContainer : listOfBacktestContainer) {
					Co.println("\n\n--> Backtest container: " + backtestContainer.symbol.name);
					backtestContainer.markAsComplete();
					//new TableController().displayTable(AsciiTables.backtest_strategy_response, BacktestUtils.getTableDisplayRows(backtestContainer));
					// Co.print(new ExportTools().exportToString(AsciiTables.backtest_strategy_response, listOfDisplayRows));
					BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(backtestContainer);
					Co.println(backtestEvaluation.toString());
				}
			}

			if (listenerOfMainBacktestCompleted != null) {
				listenerOfMainBacktestCompleted.backtestCompleted();
			}
			if (backtestType == BacktestType.backtest_default) {
				Co.println("--> Finished backtest");
				Global.callbackLock.releaseLock();
			}
		}
	}

	public StrategyBase getStrategy() {
		return listOfBacktestContainer.get(0).algorithm.strategyBase;
	}

	public ArrayList<BacktestContainer> getListOfBacktestContainer() {
		return listOfBacktestContainer;
	}
}
