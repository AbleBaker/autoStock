package com.autoStock.backtest;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.text.DateFormatter;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.backtest.BacktestUtils.BacktestResultTransactionDetails;
import com.autoStock.guage.SignalGuage;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MiscTools;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluation {
	public BacktestResultTransactionDetails transactionDetails;
	
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
	public ArrayList<Pair<Date, Double>> listOfDailyYield;
	
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
		public String signalPointType;
		public int periodLength;
		public int maxSignalAverage;
		
		public ArrayList<DescriptorForGuage> listOfDescriptorForGuage = new ArrayList<DescriptorForGuage>();
		
		public SignalParameters signalParamaters;
		
		@Override
		public String toString() {
			String string = new String();
			string += signalName + "(" + periodLength + ", " + maxSignalAverage + "), " + signalPointType;
			string += "\n";
			
			for (DescriptorForGuage descriptorForGuage : listOfDescriptorForGuage){
				string += descriptorForGuage.toString() + "\n";
			}
			
			return string;
		}
	}
	
	public static class DescriptorForGuage {
		public String guageType;
		public String guageBoundsName;
		public String guageThreshold;
		
		public DescriptorForGuage(SignalGuage signalGuage) {
			guageType = signalGuage.mutableEnumForSignalGuageType.enumValue.name();
			guageBoundsName = signalGuage.signalBounds.name();
			guageThreshold = String.valueOf(signalGuage.threshold);
		}

		@Override
		public String toString() {
			return "   " + guageType + ", " + guageBoundsName + ", " + guageThreshold;
		}
	}
	
	public static class DescriptorForIndicator {
		public String indicatorName;
		public int indicatorPeriodLength;
		public int indicatorResultSetLength;
		
		public DescriptorForIndicator(String indicatorName, int indicatorPeriodLength, int resultsetLength) {
			this.indicatorName = indicatorName;
			this.indicatorPeriodLength = indicatorPeriodLength;
			this.indicatorResultSetLength = resultsetLength;
		}
		
		@Override
		public String toString() {
			return indicatorName + " : " + indicatorPeriodLength + ", " + indicatorResultSetLength;
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
		
		string += "***** $" +  MiscTools.getCommifiedValue(accountBalance - AccountProvider.defaultBalance) + " / %" + new DecimalFormat("#.00").format(percentYield) + " Score: " + MiscTools.getCommifiedValue(getScore()) + " *****";
		string += "\n--> Date " + DateTools.getPrettyDate(dateStart) + " to " + DateTools.getPrettyDate(dateEnd);
		string += "\n--> Transactions: " + transactions;
		string += "\n--> Transaction fees: $" + new DecimalFormat("#.00").format(transactionFeesPaid);
		string += "\n--> Transaction details: " + transactionDetails.countForTradeLongEntry + " / " + transactionDetails.countForTradeShortEntry + ", " + transactionDetails.countForTradesReentry + ", " + transactionDetails.countForTradeExit;
		string += "\n--> Transaction profit / loss: %" + new DecimalFormat("#.00").format(percentTradeWin) + ", " + transactionDetails.countForTradesProfit + ", " + transactionDetails.countForTradesLoss;
		string += "\n--> Transaction avg profit / loss: $" + new DecimalFormat("#.00").format(transactionDetails.avgTradeWin) + ", $" + new DecimalFormat("#.00").format(transactionDetails.avgTradeLoss);
		string += "\n--> Trasaction max profit, loss: " + new DecimalFormat("#.00").format(transactionDetails.maxTradeWin)
			+ ", " + new DecimalFormat("#.00").format(transactionDetails.maxTradeLoss)
			+ ", " + new DecimalFormat("#.00").format(transactionDetails.minTradeWin)
			+ ", " + new DecimalFormat("#.00").format(transactionDetails.minTradeLoss);
		
		string += "\n";
		
		for (Pair<Date, Double> pair : listOfDailyYield){
			string += "\n - Daily yield: " + new SimpleDateFormat("dd/MM/yyyy").format(pair.first) + ", %" + new DecimalFormat("#.00").format(pair.second);
		}
		
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
