package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.signal.Signal;
import com.autoStock.signal.Signal.CombinedSignal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalMetric;

/**
 * @author Kevin Kowalewski
 * 
 */
public class BacktestUtils {
//
//	public static String getCurrentBacktestValueGroup(CombinedSignal combinedSignal) {
//		return "--------> Best backtest results: \n" + 
//			"Long entry: " + combinedSignal.longEntry + "\n" + 
//			"Long exit: " + combinedSignal.longExit + "\n" + 
//			"Short entry: " + combinedSignal.shortEntry + "\n" +
//			"Short exit: " + combinedSignal.shortExit + "\n" +
//			"Period Length: " + SignalControl.periodLength + "\n" + 
//			"Period Average PPC: " + SignalControl.periodAverageForPPC + "\n" + 
//			"Period Average DI: " + SignalControl.periodAverageForDI + "\n" + 
//			"Period Average CCI: " + SignalControl.periodAverageForCCI + "\n" + 
//			"Period Average MACD: " + SignalControl.periodAverageForMACD + "\n" + 
//			"Period Average TRIX: " + SignalControl.periodAverageForTRIX + "\n" +
//			"Total transactions: " + Account.instance.getTransactions() + "\n" + 
//			"Bank account balance: " + Account.instance.getBankBalance() + "\n";
//	}
//	
	
	public static String getCurrentBacktestValueGroup(Signal signal){
		String string = "******** ........ Backtest results ........ ********\n";
		
		for (SignalMetric signalMetric : signal.getListOfSignalMetric()){
			string += "\nSignal metric: " + signalMetric.signalMetricType.name() + "\n";
			string += "  Long entry: " + signalMetric.signalMetricType.pointToSignalLongEntry + "\n";
			string += "  Long exit: " + signalMetric.signalMetricType.pointToSignalLongExit + "\n";
			string += "  Short entry: " + signalMetric.signalMetricType.pointToSignalShortEntry + "\n";
			string += "  Short exit: " + signalMetric.signalMetricType.pointToSignalShortExit + "\n";
		}
		
		string += "\nPeriod length: " + SignalControl.periodLength + "\n";
		string += "Transactions: " + Account.instance.getTransactions() + "\n";
		string += "Fees: " + Account.instance.getTransactionFeesPaid() + "\n";
		string += "Balance: " + Account.instance.getBankBalance() + "\n";
		
		return string;
	}
	
	public static void printBestBacktestResults(ArrayList<String> listOfStringBestBacktestResults){
		Co.println("Best backtest results...");
		for (String string : listOfStringBestBacktestResults){
			Co.println(string);
		}
	}
}
