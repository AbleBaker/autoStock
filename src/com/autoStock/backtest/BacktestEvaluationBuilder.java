package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentCampaignProvider;
import com.autoStock.adjust.AdjustmentOfBasicDouble;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfEnum;
import com.autoStock.adjust.AdjustmentOfSignalMetric;
import com.autoStock.algorithm.core.AlgorithmRemodeler;
import com.autoStock.backtest.BacktestEvaluation.DescriptorForAdjustment;
import com.autoStock.backtest.BacktestEvaluation.DescriptorForIndicator;
import com.autoStock.backtest.BacktestEvaluation.DescriptorForSignal;
import com.autoStock.backtest.BacktestUtils.BacktestResultTransactionDetails;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.guage.SignalGuage;
import com.autoStock.indicator.IndicatorBase;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.google.gson.internal.Pair;

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
		backtestEvaluation.backtestResultTransactionDetails = backtestResultTransactionDetails;
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
				
				if (arrayOfSignalGuage != null){
					DescriptorForSignal descriptorForGuage = new DescriptorForSignal();
					descriptorForGuage.signalName = signalBase.getClass().getSimpleName();
					descriptorForGuage.signalBoundsName = arrayOfSignalGuage[0].immutableEnumForSignalGuageType.enumValue.name();
					descriptorForGuage.signalBoundsType = arrayOfSignalGuage[0].signalBounds.name();
					descriptorForGuage.signalPointType = pair.first.name();
					descriptorForGuage.signalBoundsThreshold = arrayOfSignalGuage[0].threshold;
					
					descriptorForGuage.periodLength = signalBase.signalParameters.periodLength.value;
					descriptorForGuage.maxSignalAverage = signalBase.signalParameters.maxSignalAverage.value;
					
					backtestEvaluation.listOfDescriptorForSignal.add(descriptorForGuage);
				}
			}
		}
		
		for (IndicatorBase indicatorBase : backtestContainer.algorithm.signalGroup.getIndicatorGroup().getListOfIndicatorBaseActive()){
			DescriptorForIndicator descriptorForIndicator = new DescriptorForIndicator(indicatorBase.getClass().getSimpleName(), indicatorBase.periodLength.value);
			backtestEvaluation.listOfDescriptorForIndicator.add(descriptorForIndicator);
		}
		
		if (AdjustmentCampaignProvider.getInstance().getAdjustmentCampaignForAlgorithm(backtestContainer.symbol) != null){
			for (AdjustmentBase adjustmentBase : AdjustmentCampaignProvider.getInstance().getAdjustmentCampaignForAlgorithm(backtestContainer.symbol).getListOfAdjustmentBase()){
				DescriptorForAdjustment descriptorForAdjustment = new DescriptorForAdjustment();
				descriptorForAdjustment.adjustmentType = adjustmentBase.getClass().getSimpleName();
				descriptorForAdjustment.adjustmentDescription = adjustmentBase.getDescription();
				
				if (adjustmentBase instanceof AdjustmentOfBasicInteger){
					descriptorForAdjustment.adjustmentValue = String.valueOf(((AdjustmentOfBasicInteger)adjustmentBase).getValue());
				}else if (adjustmentBase instanceof AdjustmentOfBasicDouble){
					descriptorForAdjustment.adjustmentValue = String.valueOf(((AdjustmentOfBasicDouble)adjustmentBase).getValue());
				}else if (adjustmentBase instanceof AdjustmentOfEnum){
					descriptorForAdjustment.adjustmentValue = ((AdjustmentOfEnum)adjustmentBase).getValue().name();
				}else if (adjustmentBase instanceof AdjustmentOfSignalMetric){
					descriptorForAdjustment.adjustmentValue = String.valueOf(((AdjustmentOfSignalMetric)adjustmentBase).getValue());
				}else{
					throw new UnsupportedOperationException("Unknown adjustment class: " + adjustmentBase.getClass().getName());
				}
				
				backtestEvaluation.listOfDescriptorForAdjustment.add(descriptorForAdjustment);
			}
		}
		
		backtestEvaluation.listOfDisplayRowsFromStrategyResponse = BacktestUtils.getTableDisplayRows(backtestContainer);
		
		return backtestEvaluation;
	}
	
	public BacktestEvaluation buildOutOfSampleEvaluation(BacktestEvaluation backtestEvaluation){	
		Co.println("--> Building out of sample evaluation");
		Date dateStart = DateTools.getFirstWeekdayAfter(backtestEvaluation.dateEnd);
		Date dateEnd = (Date) dateStart.clone();
		
		dateStart.setHours(backtestEvaluation.exchange.timeOpenForeign.hours);
		dateStart.setMinutes(backtestEvaluation.exchange.timeOpenForeign.minutes);
		dateEnd.setHours(backtestEvaluation.exchange.timeCloseForeign.hours);
		dateEnd.setMinutes(backtestEvaluation.exchange.timeCloseForeign.minutes);
		
		HistoricalData historicalData = new HistoricalData(backtestEvaluation.exchange, backtestEvaluation.symbol, dateStart, dateEnd, Resolution.min);
		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.symbolName), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));
		
		SingleBacktest singleBacktest = new SingleBacktest(historicalData);
		
		new AlgorithmRemodeler(singleBacktest.backtestContainer.algorithm, backtestEvaluation.algorithmModel);
		
		singleBacktest.setBacktestData(listOfResults);
		singleBacktest.runBacktest();
			
		Co.println("********");
		Co.println(buildEvaluation(singleBacktest.backtestContainer).toString());
		
		return buildEvaluation(singleBacktest.backtestContainer);
	}
}
