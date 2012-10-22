package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalMetric;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.MiscTools;

/**
 * @author Kevin Kowalewski
 * 
 */
public class BacktestUtils {
	public synchronized static String getCurrentBacktestValueGroup(Signal signal){
		String string = "******** ........ Backtest results ........ ********\n";
		
		for (SignalMetric signalMetric : signal.getListOfSignalMetric()){
			string += "\nSignal metric: " + signalMetric.signalMetricType.name() + "\n";
			string += "  Long entry: " + signalMetric.signalMetricType.pointToSignalLongEntry + "\n";
			string += "  Long exit: " + signalMetric.signalMetricType.pointToSignalLongExit + "\n";
			string += "  Short entry: " + signalMetric.signalMetricType.pointToSignalShortEntry + "\n";
			string += "  Short exit: " + signalMetric.signalMetricType.pointToSignalShortExit + "\n";
		}
		
		string += "\nPeriod length: " + SignalControl.periodLengthStart + "\n";
		string += "Transactions: " + Account.getInstance().getTransactions() + "\n";
		string += "Fees: " + Account.getInstance().getTransactionFeesPaid() + "\n";
		string += "Balance: " + Account.getInstance().getAccountBalance() + "\n";
		
		return string;
	}
	
	public static String getCurrentBacktestCompleteValueGroup(Signal signal, StrategyOptions strategyOptions, int countForTradesProfit, int countForTradesLoss, int countForReentry){
		String string = "\n ******* Backtest results $" + MiscTools.getCommifiedValue(Account.getInstance().getAccountBalance()) + " ********";
		
		string += "\n --> Balance: " + MiscTools.getCommifiedValue(Account.getInstance().getAccountBalance());
		string += "\n --> Transactions: " + Account.getInstance().getTransactions();
		string += "\n --> Fees: " + MiscTools.getCommifiedValue(Account.getInstance().getTransactionFeesPaid());
		
		string += "\n --> Transactions Profit / Loss: " + " %" + MathTools.round((double)countForTradesProfit / (double)(countForTradesProfit + countForTradesLoss)) + ", " + countForTradesProfit + ", " + countForTradesLoss;
		string += "\n --> Reentered: " + countForReentry;
		
		string += "\n --> SignalControl: " + SignalControl.periodLengthStart + ", " + SignalControl.periodLengthMiddle + ", " + SignalControl.periodLengthEnd;
		
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
		string += "\n Max stop loss value ($): " +  strategyOptions.maxStopLossValue;
		string += "\n Max transactions per day: " + strategyOptions.maxTransactionsDay;
		string += "\n Min take profit exit: " + strategyOptions.minTakeProfitExit;
		string += "\n Signal point tactic (entry): " + strategyOptions.signalPointTacticForEntry.name();
		string += "\n Signal point tactic (reentry): " + strategyOptions.signalPointTacticForReentry.name();
		string += "\n Signal point tactic (exit): " + strategyOptions.signalPointTacticForExit.name();
		string += "\n Taper period length: " + strategyOptions.taperPeriodLength;
		string += "\n Reentry interval minutes: " + strategyOptions.intervalForReentryMins;
		string += "\n Reentry maximum frequency: " + strategyOptions.maxReenterTimes;
		
		string += "\n\n";
		
		return string;
	}
	
	public static void printBestBacktestResults(ArrayList<String> listOfStringBestBacktestResults){
		Co.println("Best backtest results...");
		for (String string : listOfStringBestBacktestResults){
			Co.println(string);
		}
	}
}
