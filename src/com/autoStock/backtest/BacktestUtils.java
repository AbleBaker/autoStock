package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.account.BasicAccount;
import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentCampaignProvider;
import com.autoStock.adjust.AdjustmentIdentifier;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfEnum;
import com.autoStock.adjust.AdjustmentOfSignalMetric;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.guage.SignalGuage;
import com.autoStock.indicator.IndicatorBase;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalBase;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.MiscTools;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 * 
 */
public class BacktestUtils {	
	public static String getCurrentBacktestCompleteValueGroup(Signal signal, StrategyOptions strategyOptions, BacktestResultTransactionDetails backtestResultDetails, BacktestType backtestType, BasicAccount basicAccount){
		
		String string = "\n ******* Backtest results $" + MiscTools.getCommifiedValue(basicAccount.getBalance()) + " ********";
		
		string += "\n --> Balance: $" + MiscTools.getCommifiedValue(basicAccount.getBalance());
		string += "\n --> Total transactions: " + basicAccount.getTransactions();
		string += "\n --> Fees: $" + MiscTools.getCommifiedValue(basicAccount.getTransactionFees());
		
		string += "\n --> Entered, reentered, exited: " + + backtestResultDetails.countForTradeEntry + ", " + backtestResultDetails.countForTradesReentry + ", " + backtestResultDetails.countForTradeExit;
		string += "\n --> Transactions profit / loss: " + MathTools.round(((double)backtestResultDetails.countForTradesProfit / (double)(backtestResultDetails.countForTradesProfit + backtestResultDetails.countForTradesLoss)) * 100) + "%, " + backtestResultDetails.countForTradesProfit + ", " + backtestResultDetails.countForTradesLoss;
		
		if (basicAccount.getTransactions() > 0 && backtestResultDetails.countForTradesProfit == 0 && backtestResultDetails.countForTradesLoss == 0){
			throw new IllegalStateException("Details: " + basicAccount.getTransactions() + ", " + backtestResultDetails.countForTradesProfit + ", " + backtestResultDetails.countForTradesLoss);
		}
		
		for (SignalBase signalBase : signal.getListOfSignalBase()){
			string += "\n\n --> Signal metric: " + signalBase.signalMetricType.name() + "\n";
			
			if (signalBase.signalParameters.arrayOfSignalGuageForLongEntry != null){
				for (SignalGuage signalGuage : signalBase.signalParameters.arrayOfSignalGuageForLongEntry){
					string += " +Long entry: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.immutableEnumForSignalGuageType.enumValue.name() + "\n";
				}
			}
			
			if (signalBase.signalParameters.arrayOfSignalGuageForLongExit != null){
				for (SignalGuage signalGuage : signalBase.signalParameters.arrayOfSignalGuageForLongExit){
					string += " +Long exit: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.immutableEnumForSignalGuageType.enumValue.name() + "\n";
				}
			}
			
			if (signalBase.signalParameters.arrayOfSignalGuageForShortEntry != null){
				for (SignalGuage signalGuage : signalBase.signalParameters.arrayOfSignalGuageForShortEntry){
					string += " +Short entry: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.immutableEnumForSignalGuageType.enumValue.name() + "\n";
				}
			}
			
			if (signalBase.signalParameters.arrayOfSignalGuageForShortExit != null){
				for (SignalGuage signalGuage : signalBase.signalParameters.arrayOfSignalGuageForShortExit){
					string += " +Short exit: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.immutableEnumForSignalGuageType.enumValue.name() + "\n";
				}
			}
			
			string += "\n";
		}
		
		for (IndicatorBase indicatorBase : signal.getSignalGroup().getIndicatorGroup().getListOfIndicatorBase()){
			string += " +Indicator period: " + indicatorBase.getClass().getSimpleName() + ", " + indicatorBase.periodLength.value + "\n";
		}
		
		if (backtestType == BacktestType.backtest_adjustment_boilerplate || backtestType == BacktestType.backtest_adjustment_individual || backtestType == BacktestType.backtest_clustered_client){
			for (Pair<AdjustmentIdentifier, AdjustmentCampaign> adjustmentPair : AdjustmentCampaignProvider.getInstance().getListOfAdjustmentCampaign()){
				for (AdjustmentBase adjustmentBase : adjustmentPair.second.getListOfAdjustmentBase())
					if (adjustmentBase instanceof AdjustmentOfBasicInteger){
						string += " +AdjustmentOfBasicInteger " + adjustmentBase.getDescription() + " : " + ((AdjustmentOfBasicInteger)adjustmentBase).getValue() + "\n";
					}else if (adjustmentBase instanceof AdjustmentOfEnum){
						string += " +AdjustmentOfEnum " + adjustmentBase.getDescription() + " : " + ((AdjustmentOfEnum)adjustmentBase).getValue().name() + "\n";
					}else if (adjustmentBase instanceof AdjustmentOfSignalMetric){
						string += " +AdjustmentOfSignalMetric " + adjustmentBase.getDescription() + " : " + ((AdjustmentOfSignalMetric)adjustmentBase).getValue() + "\n";
					}
			}
		}
		
		string += "\n";
		
		string += strategyOptions.toString();
		
		string += "\n\n";
		
		return string;
	}
	
	public static void printBestBacktestResults(ArrayList<String> listOfStringBestBacktestResults){
		Co.println("Best backtest results...");
		for (String string : listOfStringBestBacktestResults){
			Co.println(string);
		}
	}
	
	public static BacktestResultTransactionDetails getProfitLossDetails(ArrayList<BacktestContainer> listOfBacktestContainer){
		BacktestResultTransactionDetails backtestProfitLossType = new BacktestResultTransactionDetails();
		
		for (BacktestContainer backtestContainer : listOfBacktestContainer){
			BacktestResultTransactionDetails backtestTransactions = getBacktestResultTransactionDetails(backtestContainer);
			
			backtestProfitLossType.countForTradeEntry += backtestTransactions.countForTradeEntry;
			backtestProfitLossType.countForTradeExit += backtestTransactions.countForTradeExit;
			backtestProfitLossType.countForTradesLoss += backtestTransactions.countForTradesLoss;
			backtestProfitLossType.countForTradesProfit += backtestTransactions.countForTradesProfit;
			backtestProfitLossType.countForTradesReentry += backtestTransactions.countForTradesReentry;
		}
		
		if (AccountProvider.getInstance().getGlobalAccount().getTransactions() > 0 && backtestProfitLossType.countForTradesLoss == 0 && backtestProfitLossType.countForTradesProfit == 0){
			throw new IllegalStateException();
		}
		
		return backtestProfitLossType;
	}
	
	private static BacktestResultTransactionDetails getBacktestResultTransactionDetails(BacktestContainer backtestContainer){
		BacktestResultTransactionDetails backtestTransactions = new BacktestResultTransactionDetails();
		
		for (StrategyResponse strategyResponse : backtestContainer.listOfStrategyResponse){
			if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
				if (strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true) > 0){
					backtestTransactions.countForTradesProfit++;
				}else if (strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true) <= 0){
					backtestTransactions.countForTradesLoss++;
				}
				
				backtestTransactions.countForTradeExit++;
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry){
				backtestTransactions.countForTradeEntry++;
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry){
				backtestTransactions.countForTradesReentry++;
			}
		}
		
		return backtestTransactions;
	}
	
	public static class BacktestResultTransactionDetails {
		public int countForTradeEntry;
		public int countForTradeExit;
		public int countForTradesProfit;
		public int countForTradesLoss;
		public int countForTradesReentry;
	}
}
