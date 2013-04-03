package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfEnum;
import com.autoStock.adjust.AdjustmentOfSignalMetric;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.finance.Account;
import com.autoStock.guage.SignalGuage;
import com.autoStock.indicator.IndicatorBase;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalControl;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.MiscTools;

/**
 * @author Kevin Kowalewski
 * 
 */
public class BacktestUtils {	
	public static String getCurrentBacktestCompleteValueGroup(Signal signal, StrategyOptions strategyOptions, BacktestResultDetails backtestResultDetails, BacktestType backtestType){
		String string = "\n ******* Backtest results $" + MiscTools.getCommifiedValue(Account.getInstance().getAccountBalance()) + " ********";
		
		string += "\n --> Balance: $" + MiscTools.getCommifiedValue(Account.getInstance().getAccountBalance());
		string += "\n --> Total transactions: " + Account.getInstance().getTransactions();
		string += "\n --> Fees: $" + MiscTools.getCommifiedValue(Account.getInstance().getTransactionFeesPaid());
		
		string += "\n --> Entered, reentered, exited: " + + backtestResultDetails.countForTradeEntry + ", " + backtestResultDetails.countForTradesReentry + ", " + backtestResultDetails.countForTradeExit;
		string += "\n --> Transactions profit / loss: " + MathTools.round(((double)backtestResultDetails.countForTradesProfit / (double)(backtestResultDetails.countForTradesProfit + backtestResultDetails.countForTradesLoss)) * 100) + "%, " + backtestResultDetails.countForTradesProfit + ", " + backtestResultDetails.countForTradesLoss;
		
		if (Account.getInstance().getTransactions() > 0 && backtestResultDetails.countForTradesProfit == 0 && backtestResultDetails.countForTradesLoss == 0){
			throw new IllegalStateException("Details: " + Account.getInstance().getTransactions() + ", " + backtestResultDetails.countForTradesProfit + ", " + backtestResultDetails.countForTradesLoss);
		}
		
		for (SignalBase signalBase : signal.getListOfSignalBase()){
			string += "\n\n --> Signal metric: " + signalBase.signalMetricType.name() + "\n";
			
			for (SignalGuage signalGuage : signalBase.signalMetricType.arrayOfSignalGuageForLongEntry){
				string += " +Long entry: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.immutableEnumForSignalGuageType.enumValue.name() + "\n";
			}
			
			for (SignalGuage signalGuage : signalBase.signalMetricType.arrayOfSignalGuageForLongExit){
				string += " +Long exit: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.immutableEnumForSignalGuageType.enumValue.name() + "\n";
			}
			
			for (SignalGuage signalGuage : signalBase.signalMetricType.arrayOfSignalGuageForShortEntry){
				string += " +Short entry: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.immutableEnumForSignalGuageType.enumValue.name() + "\n";
			}
			
			for (SignalGuage signalGuage : signalBase.signalMetricType.arrayOfSignalGuageForShortExit){
				string += " +Short exit: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.immutableEnumForSignalGuageType.enumValue.name() + "\n";
			}
		}
		
		string += "\n";
		
		for (IndicatorBase indicatorBase : signal.getSignalGroup().getIndicatorGroup().getListOfIndicatorBase()){
			string += " +Indicator period: " + indicatorBase.getClass().getSimpleName() + ", " + indicatorBase.periodLength.value + "\n";
		}
		
		if (backtestType == BacktestType.backtest_adjustment || backtestType == BacktestType.backtest_clustered_client){
			for (AdjustmentBase adjustmentBase : AdjustmentCampaign.getInstance().getListOfAdjustmentBase()){
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
		
		string += "\n Can go long: " + strategyOptions.canGoLong;
		string += "\n Can go short: " + strategyOptions.canGoShort;
		string += "\n Can reenter: " + strategyOptions.canReenter;
		string += "\n Disable after nil changes: " + strategyOptions.disableAfterNilChanges;
		string += "\n Disable after nil changes in price: " + strategyOptions.maxNilChangePrice;
		string += "\n Disable after nil changes in volume: " + strategyOptions.maxNilChangeVolume;
		string += "\n Disable after a loss: " + strategyOptions.disableAfterLoss;
		string += "\n Max position entry time: " + strategyOptions.maxPositionEntryTime;
		string += "\n Max position exit time: " + strategyOptions.maxPositionExitTime;
		string += "\n Max position taper time: " + strategyOptions.maxPositionTaperTime;
		string += "\n Max stop loss percent: " +  strategyOptions.maxStopLossPercent.value;
		string += "\n Max transactions per day: " + strategyOptions.maxTransactionsDay;
		string += "\n Signal point tactic (entry): " + strategyOptions.signalPointTacticForEntry.name();
		string += "\n Signal point tactic (reentry): " + strategyOptions.signalPointTacticForReentry.name();
		string += "\n Signal point tactic (exit): " + strategyOptions.signalPointTacticForExit.name();
		string += "\n Taper period length: " + strategyOptions.taperPeriodLength;
		string += "\n Reentry interval minutes: " + strategyOptions.intervalForReentryMins.value;
		string += "\n Reentry maximum frequency: " + strategyOptions.maxReenterTimes.value;
		string += "\n Reentry minimum gain: " + strategyOptions.minReentryPercentGain.value;
		
		string += "\n\n";
		
		return string;
	}
	
	public static void printBestBacktestResults(ArrayList<String> listOfStringBestBacktestResults){
		Co.println("Best backtest results...");
		for (String string : listOfStringBestBacktestResults){
			Co.println(string);
		}
	}
	
	public static BacktestResultDetails getProfitLossDetails(ArrayList<BacktestContainer> listOfBacktestContainer){
		BacktestResultDetails backtestProfitLossType = new BacktestResultDetails();

		for (BacktestContainer backtestContainer : listOfBacktestContainer){
			for (StrategyResponse strategyResponse : backtestContainer.listOfStrategyResponse){
				if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit
						|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
					if (strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true) > 0){
						backtestProfitLossType.countForTradesProfit++;
					}else if (strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true) <= 0){
						backtestProfitLossType.countForTradesLoss++;
					}
					
					backtestProfitLossType.countForTradeExit++;
				}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry
						|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry){
					backtestProfitLossType.countForTradeEntry++;
				}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry
						|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry){
					backtestProfitLossType.countForTradesReentry++;
				}
			}
		}
		
		if (Account.getInstance().getTransactions() > 0 && backtestProfitLossType.countForTradesLoss == 0 && backtestProfitLossType.countForTradesProfit == 0){
			throw new IllegalStateException();
		}
		
		return backtestProfitLossType;
	}
	
	public static class BacktestResultDetails {
		public int countForTradeEntry = 0;
		public int countForTradeExit = 0;
		public int countForTradesProfit = 0;
		public int countForTradesLoss = 0;
		public int countForTradesReentry = 0;
	}
}
