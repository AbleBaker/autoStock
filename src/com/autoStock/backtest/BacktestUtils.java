package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalMetric;

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
