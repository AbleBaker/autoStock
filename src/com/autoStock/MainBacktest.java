package com.autoStock;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.account.AccountProvider;
import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentCampaignProvider;
import com.autoStock.adjust.AdjustmentSeriesForAlgorithm;
import com.autoStock.adjust.AdjustmentSeriesForAlgorithmShortOnly;
import com.autoStock.adjust.AdjustmentIdentifier;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.BacktestContainer;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.BacktestEvaluator;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.backtest.BacktestUtils.BacktestResultTransactionDetails;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.ListenerOfBacktestCompleted;
import com.autoStock.backtest.ListenerOfMainBacktestCompleted;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.Global;
import com.autoStock.internal.GsonClassAdapter;
import com.autoStock.order.OrderDefinitions.OrderMode;
import com.autoStock.position.PositionGovernor;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalMoment;
import com.autoStock.strategy.StrategyBase;
import com.autoStock.strategy.StrategyOfTest;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.HistoricalDataList;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainBacktest implements ListenerOfBacktestCompleted {
	private AdjustmentCampaignProvider adjustmentCampaignProvider = AdjustmentCampaignProvider.getInstance();
	private BacktestType backtestType;
	private ArrayList<HistoricalDataList> listOfHistoricalDataList = new ArrayList<HistoricalDataList>();
	private Exchange exchange;
	private int currentBacktestDayIndex = 0;
	private double metricBestAccountBalance = 0;
	private Benchmark bench = new Benchmark();
	private AtomicInteger callbacks = new AtomicInteger();
	private AlgorithmMode algorithmMode;
	private ArrayList<BacktestContainer> listOfBacktestContainer = new ArrayList<BacktestContainer>();
	private ListenerOfMainBacktestCompleted listenerOfMainBacktestCompleted;
	private ArrayList<String> listOfStringBestBacktestResults = new ArrayList<String>();
	private BacktestEvaluator backtestEvaluator = new BacktestEvaluator();

	public MainBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols, BacktestType backtestType, ListenerOfMainBacktestCompleted listerListenerOfMainBacktestCompleted) {
		this.exchange = exchange;
		this.backtestType = backtestType;
		this.algorithmMode = AlgorithmMode.getFromBacktestType(backtestType);
		this.listenerOfMainBacktestCompleted = listerListenerOfMainBacktestCompleted;
		Global.callbackLock.requestLock();

		if (algorithmMode.displayChart) {
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

		if (algorithmMode.displayChart) {
			Global.callbackLock.requestLock();
		}

		runMainBacktest(dateStart, dateEnd, listOfSymbols);
	}

	private void runMainBacktest(Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols) {
		if (backtestType != BacktestType.backtest_result_only) {
			Co.println("Main backtest...\n\n");
		}
		
		if (listOfSymbols.size() == 0){
			throw new IllegalArgumentException("No symbols specified");
		}

		HistoricalData baseHistoricalData = new HistoricalData(exchange, null, dateStart, dateEnd, Resolution.min);

		baseHistoricalData.startDate.setHours(exchange.timeOpenForeign.hours);
		baseHistoricalData.startDate.setMinutes(exchange.timeOpenForeign.minutes);
		baseHistoricalData.endDate.setHours(exchange.timeCloseForeign.hours);
		baseHistoricalData.endDate.setMinutes(exchange.timeCloseForeign.minutes);

		ArrayList<Date> listOfBacktestDates = DateTools.getListOfDatesOnWeekdays(baseHistoricalData.startDate, baseHistoricalData.endDate);

		if (listOfBacktestDates.size() == 0) {
			throw new IllegalArgumentException("Weekday not entered");
		}

		ListTools.removeDuplicates(listOfSymbols);

		for (Date date : listOfBacktestDates) {
			HistoricalDataList historicalDataList = new HistoricalDataList();

			for (String symbol : listOfSymbols) {
				HistoricalData dayHistoricalData = new HistoricalData(exchange, new Symbol(symbol, SecurityType.type_stock), (Date) date.clone(), (Date) date.clone(), baseHistoricalData.resolution);
				dayHistoricalData.startDate.setHours(exchange.timeOpenForeign.hours);
				dayHistoricalData.startDate.setMinutes(exchange.timeOpenForeign.minutes);
				dayHistoricalData.endDate.setHours(exchange.timeCloseForeign.hours);
				dayHistoricalData.endDate.setMinutes(exchange.timeCloseForeign.minutes);
				
				historicalDataList.listOfHistoricalData.add(dayHistoricalData);
			}

			listOfHistoricalDataList.add(historicalDataList);
		}
		
		initBacktest();

		if (backtestType == BacktestType.backtest_adjustment_boilerplate) {
			adjustmentCampaignProvider.applyBoilerplateValues();
		} else if (backtestType == BacktestType.backtest_adjustment_individual) {
			for (Pair<AdjustmentIdentifier, AdjustmentCampaign> pair : adjustmentCampaignProvider.getListOfAdjustmentCampaign()) {
				pair.second.applyValues();
			}
		}
		
		runNextBacktestForDays(false);
	}

	private void initBacktest() {
		HistoricalDataList historicalDataList = listOfHistoricalDataList.get(0);

		for (HistoricalData historicalData : historicalDataList.listOfHistoricalData) {
			BacktestContainer backtestContainer = new BacktestContainer(historicalData.symbol, exchange, this, algorithmMode);
			listOfBacktestContainer.add(backtestContainer);

			AdjustmentCampaign adjustmentCampaign;

//			if (historicalData.symbol.symbolName.equals("AIG")) {
				adjustmentCampaign = new AdjustmentSeriesForAlgorithm(backtestContainer.algorithm);
//			}
//			
//			else {
//				adjustmentCampaign = new AdjustmentCampaignSeriesForAlgorithmShortOnly(backtestContainer.algorithm);
//			}
			adjustmentCampaign.initialize();
			adjustmentCampaignProvider.addAdjustmentCampaignForAlgorithm(adjustmentCampaign, historicalData.symbol);
		}
	}

	@SuppressWarnings("unchecked")
	private void runNextBacktestOnContainers(HistoricalDataList historicalDataList) {
		callbacks.set(listOfBacktestContainer.size());
		if (backtestType != BacktestType.backtest_result_only) {
			Co.println("\nBacktesting... " + currentBacktestDayIndex);
		}

		boolean backtestContainedNoData = false;

		PositionGovernor.getInstance().reset();
		PositionManager.getInstance().reset();

		for (BacktestContainer backtestContainer : listOfBacktestContainer) {
			if (backtestContainer.isIncomplete()) {
				HistoricalData historicalData = getHistoricalDataForSymbol(historicalDataList, backtestContainer.symbol.symbolName);
				ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.symbolName), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));

				if (listOfResults.size() > 0) {
					backtestContainer.setBacktestData(listOfResults, historicalData);
					backtestContainer.runBacktest();
				} else {
					Co.println("--> No backtest data! " + backtestContainer.symbol.symbolName);
					backtestContainedNoData = true;
					break;
				}
			} else {
				Co.println("--> Adjustment is already complete for: " + backtestContainer.symbol.symbolName);
				callbacks.decrementAndGet();
			}
		}

		if (backtestContainedNoData) {
			currentBacktestDayIndex++;
			runNextBacktestForDays(true);
		}
	}

	private HistoricalData getHistoricalDataForSymbol(HistoricalDataList historicalDataList, String symbol) {
		if (historicalDataList.listOfHistoricalData.size() == 0) {
			throw new IllegalStateException("Historical data list size is 0 for symbol: " + symbol);
		}
		for (HistoricalData historicalData : historicalDataList.listOfHistoricalData) {
			if (historicalData.symbol.symbolName.equals(symbol)) {
				return historicalData;
			}
		}

		throw new IllegalStateException("No symbol data found for symbol: " + symbol);
	}

	private synchronized boolean runNextBacktestForDays(boolean skippedDay) {
		if (currentBacktestDayIndex == listOfHistoricalDataList.size()) {
			if (backtestType == BacktestType.backtest_default || backtestType == BacktestType.backtest_result_only) {
				return false;
			} else if (backtestType == BacktestType.backtest_clustered_client) {
				Global.callbackLock.releaseLock();
				if (listenerOfMainBacktestCompleted != null) {
					listenerOfMainBacktestCompleted.backtestCompleted();
				}
				return false;
			}


			if (backtestType == BacktestType.backtest_adjustment_boilerplate) {
				if (AccountProvider.getInstance().getGlobalAccount().getBalance() > metricBestAccountBalance) {
					if (listOfBacktestContainer.get(0).algorithm != null) {
						BacktestResultTransactionDetails backtestDetails = BacktestUtils.getProfitLossDetails(listOfBacktestContainer);
						listOfStringBestBacktestResults.add(BacktestUtils.getCurrentBacktestCompleteValueGroup(listOfBacktestContainer.get(0).algorithm.strategyBase.signal, listOfBacktestContainer.get(0).algorithm.strategyBase.strategyOptions, backtestDetails, backtestType, AccountProvider.getInstance().getGlobalAccount()));
					}
					metricBestAccountBalance = AccountProvider.getInstance().getGlobalAccount().getBalance();
				}
				
				if (adjustmentCampaignProvider.runBoilerplateAdjustment()) {
					currentBacktestDayIndex = 0;
					AccountProvider.getInstance().getGlobalAccount().reset();

					for (BacktestContainer backtestContainer : listOfBacktestContainer) {
						backtestContainer.reset();
					}

					runNextBacktestForDays(false);
				} else {
					Co.println("******** End of backtest and adjustment ********");
					BacktestUtils.printBestBacktestResults(listOfStringBestBacktestResults);
					Global.callbackLock.releaseLock();
				}
			} else if (backtestType == BacktestType.backtest_adjustment_individual) {
				
				Co.println("--> Evaluated");
				
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

					runNextBacktestForDays(false);
				} else {
					Co.println("******** End of backtest and adjustment ********");
					
					backtestEvaluator.pruneForFinish();			
					backtestEvaluator.reverse();
					
					for (BacktestContainer backtestContainer : listOfBacktestContainer) {
						Co.println("--> SYMBOL BACKTEST: " + backtestContainer.symbol.symbolName);
						for (BacktestEvaluation backtestEvaluation : backtestEvaluator.getResults(backtestContainer.symbol)){
							Co.println("\n\n--> String representation: " + backtestEvaluation.toString());
						}
						
						//Put this somewhere else
						BacktestEvaluation bestEvaluation = backtestEvaluator.getResults(backtestContainer.symbol).get(backtestEvaluator.getResults(backtestContainer.symbol).size() -1);
						BacktestEvaluation outOfSampleEvaluation = new BacktestEvaluationBuilder().buildOutOfSampleEvaluation(backtestContainer, bestEvaluation);
						
						GsonBuilder gsonBuilder = new GsonBuilder();
						gsonBuilder.registerTypeAdapter(SignalParameters.class, new GsonClassAdapter());
						
						int gsonId = new DatabaseQuery().insert(BasicQueries.basic_insert_gson, new QueryArg(QueryArgs.gsonString, gsonBuilder.create().toJson(bestEvaluation).replace("\"", "\\\"")));
						
						new DatabaseQuery().insert(BasicQueries.basic_insert_backtest_results,
								new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(backtestContainer.historicalData.startDate)),
								new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(backtestContainer.historicalData.endDate)),
								new QueryArg(QueryArgs.runDate, DateTools.getSqlDate(new Date())),
								new QueryArg(QueryArgs.exchange, exchange.exchangeName),
								new QueryArg(QueryArgs.symbol, bestEvaluation.symbol.symbolName),
								new QueryArg(QueryArgs.balanceInBand, new DecimalFormat("#.00").format(bestEvaluation.accountBalance)),
								new QueryArg(QueryArgs.balanceOutBand, new DecimalFormat("#.00").format(outOfSampleEvaluation.accountBalance)),
								new QueryArg(QueryArgs.percentGainInBand, "0"),
								new QueryArg(QueryArgs.percentGainOutBand, "0"),
								new QueryArg(QueryArgs.tradeEntry, String.valueOf(bestEvaluation.backtestResultTransactionDetails.countForTradeEntry)),
								new QueryArg(QueryArgs.tradeReentry, String.valueOf(bestEvaluation.backtestResultTransactionDetails.countForTradesReentry)),
								new QueryArg(QueryArgs.tradeExit, String.valueOf(bestEvaluation.backtestResultTransactionDetails.countForTradeExit)),
								new QueryArg(QueryArgs.tradeWins, String.valueOf(bestEvaluation.backtestResultTransactionDetails.countForTradesProfit)),
								new QueryArg(QueryArgs.tradeLoss, String.valueOf(bestEvaluation.backtestResultTransactionDetails.countForTradesLoss)),
								new QueryArg(QueryArgs.gsonId, String.valueOf(gsonId))
								);
						
						Co.println("--> Built OK: " + outOfSampleEvaluation.accountBalance);
					}
					
					BacktestUtils.printBestBacktestResults(listOfStringBestBacktestResults);
					Global.callbackLock.releaseLock();
				}

			}
			return false;
		} else {
			HistoricalDataList historicalDataList = listOfHistoricalDataList.get(currentBacktestDayIndex);
			runNextBacktestOnContainers(historicalDataList);
			if (skippedDay == false) {
				currentBacktestDayIndex++;
			}
			return true;
		}
	}

	private boolean runAdustmentSeries() {
		boolean adjustmentRun = false;

		for (Pair<AdjustmentIdentifier, AdjustmentCampaign> pair : adjustmentCampaignProvider.getListOfAdjustmentCampaign()) {
			if (pair.second.hasMore()) {
				pair.second.runAdjustment();
				adjustmentRun = true;
			} else {
				for (BacktestContainer backtestContainer : listOfBacktestContainer) {
					if (backtestContainer.symbol.symbolName.equals(pair.first.identifier.symbolName)) {
						// Co.println("--> Algorithm marked as complete: " + backtestContainer.symbol.symbolName);
						backtestContainer.markAsComplete();
					}
				}
			}
		}

		return adjustmentRun;
	}

	@Override
	public synchronized void backtestCompleted(Symbol symbol, AlgorithmBase algorithmBase) {
		if (backtestType != BacktestType.backtest_result_only) {
			Co.print("[ " + symbol.symbolName + " ] ");
		}

		if (callbacks.decrementAndGet() == 0) {
			if (backtestType != BacktestType.backtest_result_only) {
				Co.println("--> All called back...");
				bench.printTick("Backtested");
			}

			PositionManager.getInstance().executeExitAll();

			if (runNextBacktestForDays(false) == false) {
				if (backtestType == BacktestType.backtest_default) {
					for (BacktestContainer backtestContainer : listOfBacktestContainer) {
						Co.println("\n\n--> Backtest container: " + backtestContainer.symbol.symbolName);
						ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();

						for (StrategyResponse strategyResponse : backtestContainer.listOfStrategyResponse) {
							ArrayList<String> listOfString = new ArrayList<String>();
							listOfString.add(DateTools.getPrettyDate(strategyResponse.quoteSlice.dateTime));
							listOfString.add(backtestContainer.symbol.symbolName);
							listOfString.add(new DecimalFormat("#.00").format(strategyResponse.quoteSlice.priceClose));
							listOfString.add(strategyResponse.strategyAction.name() + ", " + strategyResponse.strategyActionCause.name());
							listOfString.add(strategyResponse.positionGovernorResponse.status.name());

							String stringForSignal = new String();

							for (SignalMoment signalMoment : strategyResponse.signal.getListOfSignalMoment()) {
								stringForSignal += signalMoment.signalMetricType.name() + ":" + signalMoment.strength + ", ";
							}

							listOfString.add(stringForSignal);

							if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit) {
								listOfString.add("$ " + new DecimalFormat("#.00").format(strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true)));
							} else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry) {
								listOfString.add("-");
							} else {
								listOfString.add("-");
							}

							listOfDisplayRows.add(listOfString);
						}
						new TableController().displayTable(AsciiTables.backtest_strategy_response, listOfDisplayRows);
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
	}

	public StrategyBase getStrategy() {
		return listOfBacktestContainer.get(0).algorithm.strategyBase;
	}

	public ArrayList<BacktestContainer> getListOfBacktestContainer() {
		return listOfBacktestContainer;
	}
}
