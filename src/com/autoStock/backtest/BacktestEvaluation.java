package com.autoStock.backtest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import com.autoStock.account.AccountProvider;
import com.autoStock.backtest.BacktestUtils.BacktestResultTransactionDetails;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MiscTools;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluation {
	public BacktestResultTransactionDetails backtestResultTransactionDetails;
	
	public final Symbol symbol;
	public final Exchange exchange;
	public final Date dateStart;
	public final Date dateEnd;
	
	public int transactions;
	public double transactionFeesPaid;
	public double accountBalance;
	public double percentGain;
	public double percentTradeWin;
	public double percentTradeLoss;
	public double percentYield;
	
	public AlgorithmModel algorithmModel = new AlgorithmModel();
	
	public ArrayList<DescriptorForSignal> listOfDescriptorForSignal = new ArrayList<DescriptorForSignal>();
	public ArrayList<DescriptorForIndicator> listOfDescriptorForIndicator = new ArrayList<DescriptorForIndicator>();
	public ArrayList<DescriptorForAdjustment> listOfDescriptorForAdjustment = new ArrayList<DescriptorForAdjustment>();
	
	public ArrayList<ArrayList<String>> listOfDisplayRowsFromStrategyResponse;
	
//	public BacktestEvaluation(Symbol symbol, Exchange exchange, Date dateStart, Date dateEnd){
//		this.symbol = symbol;
//		this.exchange = exchange;
//		this.dateStart = dateStart;
//		this.dateEnd = dateEnd;
//	}
	
	public BacktestEvaluation(BacktestContainer backtestContainer){
		this.symbol = backtestContainer.symbol;
		this.exchange = backtestContainer.exchange;
		this.dateStart = backtestContainer.dateContainerStart;
		this.dateEnd = backtestContainer.dateContainerEnd;		
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
		public int indicatorPeriodLength;
		
		public DescriptorForIndicator(String indicatorName, int indicatorPeriodLength) {
			this.indicatorName = indicatorName;
			this.indicatorPeriodLength = indicatorPeriodLength;
		}
		
		@Override
		public String toString() {
			return indicatorName + " : " + indicatorPeriodLength;
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
		
		if (transactions > 0 && listOfDisplayRowsFromStrategyResponse.size() == 0){
			throw new IllegalStateException("List can't be zero sized... Transactions: " + transactions);
		}
		
		string += "***** $" +  MiscTools.getCommifiedValue(accountBalance - AccountProvider.defaultBalance) + " / $" + MiscTools.getCommifiedValue(accountBalance) + "  Score: " + MiscTools.getCommifiedValue(getScore()) + " *****";
		string += "\n--> Date " + DateTools.getPrettyDate(dateStart) + " to " + DateTools.getPrettyDate(dateEnd);
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
		
		string += algorithmModel.strategyOptions.toString();
		
//		string += "Sizes: " + listOfDisplayRowsFromStrategyResponse.size() + ", " + listOfDisplayRowsFromStrategyResponse.get(0).size();
		
		string += "\n" + (listOfDisplayRowsFromStrategyResponse.size() == 0 ? "No transactions occurred" : new TableController().getTable(AsciiTables.backtest_strategy_response, listOfDisplayRowsFromStrategyResponse));
		
		return string;
	}
}
