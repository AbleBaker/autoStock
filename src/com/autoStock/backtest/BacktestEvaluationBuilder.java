package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Date;

import org.encog.ml.MLRegression;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentCampaignProvider;
import com.autoStock.adjust.AdjustmentOfBasicDouble;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfEnum;
import com.autoStock.adjust.AdjustmentOfSignalMetricThreshold;
import com.autoStock.algorithm.core.AlgorithmRemodeler;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.BacktestEvaluation.DescriptorForAdjustment;
import com.autoStock.backtest.BacktestEvaluation.DescriptorForGuage;
import com.autoStock.backtest.BacktestEvaluation.DescriptorForIndicator;
import com.autoStock.backtest.BacktestEvaluation.DescriptorForSignal;
import com.autoStock.backtest.BacktestUtils.BacktestDayResultDetails;
import com.autoStock.backtest.BacktestUtils.BacktestResultTransactionDetails;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.guage.SignalGuage;
import com.autoStock.indicator.IndicatorBase;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.google.gson.internal.Pair;
import com.rits.cloning.Cloner;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluationBuilder {
	public BacktestEvaluation buildEvaluation(BacktestContainer backtestContainer){
		BacktestEvaluation backtestEvaluation = new BacktestEvaluation(backtestContainer);
		
		BacktestResultTransactionDetails backtestResultTransactionDetails = BacktestUtils.getBacktestResultTransactionDetails(backtestContainer);
		
		backtestEvaluation.transactions = backtestContainer.algorithm.basicAccount.getTransactions();
		backtestEvaluation.transactionFeesPaid = backtestContainer.algorithm.basicAccount.getTransactionFees();
		backtestEvaluation.transactionDetails = backtestResultTransactionDetails;
		backtestEvaluation.accountBalance = backtestContainer.algorithm.basicAccount.getBalance();
		backtestEvaluation.percentGain = backtestContainer.algorithm.basicAccount.getBalance() / AccountProvider.getInstance().defaultBalance;
		if (backtestResultTransactionDetails.countForTradesProfit > 0){backtestEvaluation.percentTradeWin = 100 * (double)backtestResultTransactionDetails.countForTradesProfit / (double)backtestResultTransactionDetails.countForTradeExit;}
		if (backtestResultTransactionDetails.countForTradesLoss > 0){backtestEvaluation.percentTradeLoss = 100 * (double)backtestResultTransactionDetails.countForTradesLoss / (double)backtestResultTransactionDetails.countForTradeExit;}
		backtestEvaluation.percentYield = backtestContainer.algorithm.getCurrentYield();
		
		backtestEvaluation.algorithmModel.strategyOptions = backtestContainer.algorithm.strategyBase.strategyOptions.copy();
		
		for (SignalBase signalBase : backtestContainer.algorithm.strategyBase.signal.getListOfSignalBase()){
			ArrayList<Pair<SignalPointType, SignalGuage[]>> list = signalBase.signalParameters.getGuages();
			
			backtestEvaluation.algorithmModel.listOfSignalParameters.add(signalBase.signalParameters.copy());
			
			for (Pair<SignalPointType, SignalGuage[]> pair : list){
				SignalGuage[] arrayOfSignalGuage = pair.second;
				
				DescriptorForSignal descriptorForSignal = new DescriptorForSignal();
				descriptorForSignal.signalName = signalBase.getClass().getSimpleName();
				descriptorForSignal.signalPointType = pair.first.name();
				descriptorForSignal.periodLength = signalBase.signalParameters.periodLength.value;
				descriptorForSignal.maxSignalAverage = signalBase.signalParameters.maxSignalAverage.value;
				
				if (arrayOfSignalGuage != null){
					for (SignalGuage signalGuage : arrayOfSignalGuage){
						descriptorForSignal.listOfDescriptorForGuage.add(new DescriptorForGuage(signalGuage));
					}
				}
				
				backtestEvaluation.listOfDescriptorForSignal.add(descriptorForSignal);
			}
		}
		
		for (IndicatorBase indicatorBase : backtestContainer.algorithm.signalGroup.getIndicatorGroup().getListOfIndicatorBaseActive()){
			DescriptorForIndicator descriptorForIndicator = new DescriptorForIndicator(indicatorBase.getClass().getSimpleName(), indicatorBase.periodLength.value, indicatorBase.resultsetLength);
			backtestEvaluation.listOfDescriptorForIndicator.add(descriptorForIndicator);
		}
		
		if (AdjustmentCampaignProvider.getInstance().getAdjustmentCampaignForAlgorithm(backtestContainer.symbol) != null){
			for (AdjustmentBase adjustmentBase : AdjustmentCampaignProvider.getInstance().getAdjustmentCampaignForAlgorithm(backtestContainer.symbol).getListOfAdjustmentBase()){
				backtestEvaluation.listOfDescriptorForAdjustment.add(getAdjustmentDescriptor(adjustmentBase));
			}
		}
		
		backtestEvaluation.listOfDisplayRowsFromStrategyResponse = BacktestUtils.getTableDisplayRows(backtestContainer);
		
		backtestEvaluation.listOfDailyYield = new Cloner().deepClone(backtestContainer.listOfYield);
		
		return backtestEvaluation;
	}
	
	public DescriptorForAdjustment getAdjustmentDescriptor(AdjustmentBase adjustmentBase){
		DescriptorForAdjustment descriptorForAdjustment = new DescriptorForAdjustment();
		descriptorForAdjustment.adjustmentType = adjustmentBase.getClass().getSimpleName();
		descriptorForAdjustment.adjustmentDescription = adjustmentBase.getDescription();
		
		if (adjustmentBase instanceof AdjustmentOfBasicInteger){
			descriptorForAdjustment.adjustmentValue = String.valueOf(((AdjustmentOfBasicInteger)adjustmentBase).getValue());
		}else if (adjustmentBase instanceof AdjustmentOfBasicDouble){
			descriptorForAdjustment.adjustmentValue = String.valueOf(((AdjustmentOfBasicDouble)adjustmentBase).getValue());
		}else if (adjustmentBase instanceof AdjustmentOfEnum){
			descriptorForAdjustment.adjustmentValue = ((AdjustmentOfEnum)adjustmentBase).getValue().name();
		}else if (adjustmentBase instanceof AdjustmentOfSignalMetricThreshold){
			descriptorForAdjustment.adjustmentValue = String.valueOf(((AdjustmentOfSignalMetricThreshold)adjustmentBase).getValue());
		}else{
			throw new UnsupportedOperationException("Unknown adjustment class: " + adjustmentBase.getClass().getName());
		}
		
		return descriptorForAdjustment;
	}
	
	public BacktestEvaluation buildOutOfSampleEvaluation(BacktestEvaluation backtestEvaluation){
		return buildOutOfSampleEvaluation(backtestEvaluation, null);
	}
	
	public BacktestEvaluation buildOutOfSampleEvaluation(BacktestEvaluation backtestEvaluation, MLRegression neuralNetwork){	
//		Co.println("--> Building out of sample evaluation");
		Date dateStart = DateTools.getFirstWeekdayAfter(backtestEvaluation.dateEnd);
		Date dateEnd = (Date) dateStart.clone();
	   
		HistoricalData historicalData = new HistoricalData(backtestEvaluation.exchange, backtestEvaluation.symbol, dateStart, dateEnd, Resolution.min);
		historicalData.setStartAndEndDatesToExchange();
		
		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.symbolName), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));
		
		while (listOfResults.size() == 0){
//			Co.println("Couldn't find any out of sample data for: " + backtestEvaluation.symbol.symbolName + " on " + historicalData.startDate);
			
			historicalData.startDate = DateTools.getFirstWeekdayAfter(historicalData.startDate);
			historicalData.endDate = new Date(historicalData.startDate.getTime());
			historicalData.setStartAndEndDatesToExchange();

			listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.symbolName), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));			
		}
		
		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_silent);
		
		new AlgorithmRemodeler(singleBacktest.backtestContainer.algorithm, backtestEvaluation.algorithmModel).remodel();
		singleBacktest.setBacktestData(listOfResults);
		
		if (neuralNetwork != null){
			singleBacktest.backtestContainer.algorithm.signalGroup.signalOfEncog.setNetwork(neuralNetwork);
		}
		
		singleBacktest.runBacktest();
		
		return buildEvaluation(singleBacktest.backtestContainer);
	}
}
