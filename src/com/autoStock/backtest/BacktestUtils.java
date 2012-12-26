package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalMetric;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.MiscTools;

/**
 * @author Kevin Kowalewski
 * 
 */
public class BacktestUtils {	
	public static String getCurrentBacktestCompleteValueGroup(Signal signal, StrategyOptions strategyOptions, int countForTradesProfit, int countForTradesLoss, int countForReentry){
		String string = "\n ******* Backtest results $" + MiscTools.getCommifiedValue(Account.getInstance().getAccountBalance()) + " ********";
		
		string += "\n --> Balance: $" + MiscTools.getCommifiedValue(Account.getInstance().getAccountBalance());
		string += "\n --> Transactions: " + Account.getInstance().getTransactions();
		string += "\n --> Fees: $" + MiscTools.getCommifiedValue(Account.getInstance().getTransactionFeesPaid());
		
		string += "\n --> Transactions Profit / Loss: " + MathTools.round(((double)countForTradesProfit / (double)(countForTradesProfit + countForTradesLoss)) * 100) + "%, " + countForTradesProfit + ", " + countForTradesLoss;
		string += "\n --> Reentered: " + countForReentry;
		
		if (Account.getInstance().getTransactions() > 0 && countForTradesProfit == 0 && countForTradesLoss == 0){
			throw new IllegalStateException("Details: " + Account.getInstance().getTransactions() + ", " + countForTradesProfit + ", " + countForTradesLoss);
		}
		
		string += "\n --> SignalControl: " + SignalControl.periodLengthStart.value + ", " + SignalControl.periodLengthMiddle.value + ", " + SignalControl.periodLengthEnd.value;
		
		for (SignalMetric signalMetric : signal.getListOfSignalMetric()){
			string += "\n\n --> Signal metric: " + signalMetric.signalMetricType.name() + "\n";
			string += " +Long entry: " + signalMetric.signalMetricType.pointToSignalLongEntry + "\n";
			string += " +Long exit: " + signalMetric.signalMetricType.pointToSignalLongExit + "\n";
			string += " +Short entry: " + signalMetric.signalMetricType.pointToSignalShortEntry + "\n";
			string += " +Short exit: " + signalMetric.signalMetricType.pointToSignalShortExit + "\n";
		}
		
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
		string += "\n Max stop loss value ($): " +  strategyOptions.maxStopLossValue.value;
		string += "\n Max transactions per day: " + strategyOptions.maxTransactionsDay;
		string += "\n Min take profit exit: " + strategyOptions.minTakeProfitExit;
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
				if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit){
					if (strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true) > 0){
						backtestProfitLossType.countForTradesProfit++;
					}else if (strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true) <= 0){
						backtestProfitLossType.countForTradesLoss++;
					}
				}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry){
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
		public int countForTradesProfit = 0;
		public int countForTradesLoss = 0;
		public int countForTradesReentry = 0;
	}
}
