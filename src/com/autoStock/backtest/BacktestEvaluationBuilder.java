package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.autoStock.account.AccountProvider;
import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentCampaignProvider;
import com.autoStock.adjust.AdjustmentIdentifier;
import com.autoStock.adjust.AdjustmentOfBasicDouble;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfEnum;
import com.autoStock.adjust.AdjustmentOfSignalMetric;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.backtest.BacktestEvaluation.DescriptorForAdjustment;
import com.autoStock.backtest.BacktestEvaluation.DescriptorForIndicator;
import com.autoStock.backtest.BacktestEvaluation.DescriptorForSignal;
import com.autoStock.backtest.BacktestUtils.BacktestResultTransactionDetails;
import com.autoStock.guage.SignalGuage;
import com.autoStock.indicator.IndicatorBase;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluationBuilder {
	public BacktestEvaluation buildEvaluation(BacktestContainer backtestContainer){
		BacktestEvaluation backtestEvaluation = new BacktestEvaluation();
		
		BacktestResultTransactionDetails backtestResultTransactionDetails = BacktestUtils.getBacktestResultTransactionDetails(backtestContainer);
		
		backtestEvaluation.transactions = backtestContainer.algorithm.basicAccount.getTransactions();
		backtestEvaluation.transactionFeesPaid = backtestContainer.algorithm.basicAccount.getTransactionFees();
		backtestEvaluation.backtestResultTransactionDetails = backtestResultTransactionDetails;
		backtestEvaluation.accountBalance = backtestContainer.algorithm.basicAccount.getBalance();
		backtestEvaluation.percentGain = backtestContainer.algorithm.basicAccount.getBalance() / AccountProvider.getInstance().defaultBalance;
		if (backtestResultTransactionDetails.countForTradesProfit > 0){backtestEvaluation.percentTradeWin = 100 * (double)backtestResultTransactionDetails.countForTradesProfit / (double)backtestResultTransactionDetails.countForTradeExit;}
		if (backtestResultTransactionDetails.countForTradesLoss > 0){backtestEvaluation.percentTradeLoss = 100 * (double)backtestResultTransactionDetails.countForTradesLoss / (double)backtestResultTransactionDetails.countForTradeExit;}
		
		backtestEvaluation.strategyOptions = backtestContainer.algorithm.strategyBase.strategyOptions.copy();
		
		for (SignalBase signalBase : backtestContainer.algorithm.strategyBase.signal.getListOfSignalBase()){
			
			HashMap<SignalPointType, SignalGuage[]> hash = signalBase.signalParameters.getGuages();
			
			for (SignalPointType signalPointType : hash.keySet()){
				SignalGuage[] arrayOfSignalGuage = hash.get(signalPointType);
				if (arrayOfSignalGuage != null){
					DescriptorForSignal descriptorForGuage = new DescriptorForSignal();
					descriptorForGuage.signalName = signalBase.getClass().getSimpleName();
					descriptorForGuage.signalBoundsName = arrayOfSignalGuage[0].immutableEnumForSignalGuageType.enumValue.name();
					descriptorForGuage.signalBoundsType = arrayOfSignalGuage[0].signalBounds.name();
					descriptorForGuage.signalPointType = signalPointType.name();
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
		
		return backtestEvaluation;
	}
}
