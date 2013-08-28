package com.autoStock.backtest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import com.autoStock.account.AccountProvider;
import com.autoStock.adjust.AdjustmentOfPortable;
import com.autoStock.backtest.BacktestDetails.DescriptorForGuage;
import com.autoStock.backtest.BacktestUtils.BacktestResultTransactionDetails;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.MiscTools;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluation {
	public BacktestResultTransactionDetails backtestResultTransactionDetails;
	public StrategyOptions strategyOptions;
	
	public final Symbol symbol;
	public final Exchange exchange;
	
	public int transactions;
	public double transactionFeesPaid;
	public double accountBalance;
	public double percentGain;
	public double percentTradeWin;
	public double percentTradeLoss;
	
	public ArrayList<SignalParameters> listOfSignalParameters = new ArrayList<SignalParameters>();
	
	public ArrayList<DescriptorForSignal> listOfDescriptorForSignal = new ArrayList<DescriptorForSignal>();
	public ArrayList<DescriptorForIndicator> listOfDescriptorForIndicator = new ArrayList<DescriptorForIndicator>();
	public ArrayList<DescriptorForAdjustment> listOfDescriptorForAdjustment = new ArrayList<DescriptorForAdjustment>();
	
	public BacktestEvaluation(Symbol symbol, Exchange exchange){
		this.symbol = symbol;
		this.exchange = exchange;
	}
	
	public double getScore(){
		return BacktestScoreProvider.getScore(this);
	}
	
	public static class DescriptorForSignal {
		public String signalName;
		public String signalBoundsName;
		public String signalBoundsType;
		public String signalPointType;
		public double signalBoundsThreshold;
		public int periodLength;
		public int maxSignalAverage;
		
		public SignalParameters signalParamaters;
		
		@Override
		public String toString() {
			String string = new String();
			string += signalName + "(" + periodLength + ", " + maxSignalAverage + "), " + signalBoundsName + ", " + signalBoundsType + ", " + signalPointType + ", " + signalBoundsThreshold;
			return string;
		}
	}
	
	public static class DescriptorForIndicator {
		public String indicatorName;
		public int indicatorResultLength;
		
		public DescriptorForIndicator(String indicatorName, int indicatorResultLength) {
			this.indicatorName = indicatorName;
			this.indicatorResultLength = indicatorResultLength;
		}
		
		@Override
		public String toString() {
			return indicatorName + ", " + indicatorResultLength;
		}
	}
	
	public static class DescriptorForAdjustment {
		public String adjustmentType;
		public String adjustmentDescription;
		public String adjustmentValue;
		
		@Override
		public String toString() {
			return adjustmentType + ", " + adjustmentDescription + ", " + adjustmentValue;
		}
	}
	
	@Override
	public String toString() {
		String string = new String();
		
		string += "---------- $" + MiscTools.getCommifiedValue(accountBalance) + " / " + MiscTools.getCommifiedValue(getScore()) + " ----------";
		string += "\n--> Transactions: " + transactions;
		string += "\n--> Transaction fees: $" + new DecimalFormat("#.00").format(transactionFeesPaid);
		string += "\n--> Transaction details: " + backtestResultTransactionDetails.countForTradeEntry + ", " + backtestResultTransactionDetails.countForTradesReentry + ", " + backtestResultTransactionDetails.countForTradeExit;
		string += "\n--> Transaction profit / loss: " + percentTradeWin + "%, " + backtestResultTransactionDetails.countForTradesProfit + ", " + backtestResultTransactionDetails.countForTradesLoss;
		string += "\n--> Transaction avg profit / loss: $" + new DecimalFormat("#.00").format(backtestResultTransactionDetails.avgTradeWin) + ", $" + new DecimalFormat("#.00").format(backtestResultTransactionDetails.avgTradeLoss);
		string += "\n--> Trasaction max profit, loss: " + new DecimalFormat("#.00").format(backtestResultTransactionDetails.maxTradeWin)
			+ ", " + new DecimalFormat("#.00").format(backtestResultTransactionDetails.maxTradeLoss)
			+ ", " + new DecimalFormat("#.00").format(backtestResultTransactionDetails.minTradeWin)
			+ ", " + new DecimalFormat("#.00").format(backtestResultTransactionDetails.minTradeLoss);
		
		string += "\n";
		
		for (DescriptorForSignal descriptorForSignal : listOfDescriptorForSignal){
			string += "\n - " + descriptorForSignal.toString();
		}
		
		string += "\n";
		
		for (DescriptorForIndicator descriptorForIndicator : listOfDescriptorForIndicator){
			string += "\n - " + descriptorForIndicator.toString();
		}
		
		string += "\n";
		
		for (DescriptorForAdjustment descriptorForAdjustment : listOfDescriptorForAdjustment){
			string += "\n - " + descriptorForAdjustment.toString();
		}
		
		string += "\n";
		
		string += strategyOptions.toString();
		
		return string;
	}
}
