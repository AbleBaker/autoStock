package com.autoStock.backtest;

import java.text.DecimalFormat;
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
import com.autoStock.signal.SignalMoment;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.DateTools;
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
	
	public static BacktestResultTransactionDetails getBacktestResultTransactionDetails(BacktestContainer backtestContainer){
		BacktestResultTransactionDetails backtestTransactions = new BacktestResultTransactionDetails();
		
		for (StrategyResponse strategyResponse : backtestContainer.listOfStrategyResponse){
			if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit
				|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
				
				double transactionYield = strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true);
				
				if (transactionYield > 0){
					backtestTransactions.countForTradesProfit++;
					backtestTransactions.avgTradeWin += transactionYield;
					if (transactionYield > backtestTransactions.maxTradeWin){backtestTransactions.maxTradeWin = transactionYield;}
					if (transactionYield < backtestTransactions.minTradeWin || backtestTransactions.minTradeWin == 0){backtestTransactions.minTradeWin = transactionYield;}
				}else if (transactionYield <= 0){
					backtestTransactions.countForTradesLoss++;
					backtestTransactions.avgTradeLoss += transactionYield;
					
					if (transactionYield < backtestTransactions.maxTradeLoss){backtestTransactions.maxTradeLoss = transactionYield;}
					if (transactionYield > backtestTransactions.minTradeLoss || backtestTransactions.minTradeLoss == 0){backtestTransactions.minTradeLoss = transactionYield;}
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
		
		if (backtestTransactions.countForTradesProfit > 0){backtestTransactions.avgTradeWin /= backtestTransactions.countForTradesProfit;}
//		if (backtestTransactions.countForTradesLoss > 0){backtestTransactions.avgTradeLoss /= backtestTransactions.countForTradesLoss;}
		
		return backtestTransactions;
	}
	
	public static ArrayList<ArrayList<String>> getTableDisplayRows(BacktestContainer backtestContainer){
		ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();

		for (StrategyResponse strategyResponse : backtestContainer.listOfStrategyResponse) {
			ArrayList<String> listOfString = new ArrayList<String>();
			listOfString.add(DateTools.getPrettyDate(strategyResponse.quoteSlice.dateTime));
			listOfString.add(backtestContainer.symbol.symbolName);
			listOfString.add(new DecimalFormat("#.00").format(strategyResponse.quoteSlice.priceClose));
			listOfString.add(strategyResponse.strategyAction.name() + ", " + strategyResponse.strategyActionCause.name());
			listOfString.add(strategyResponse.positionGovernorResponse.status.name());

			String stringForSignal = new String();

			for (SignalMoment signalMoment : strategyResponse.signal.getListOfSignalMoment()) {
				stringForSignal += signalMoment.signalMetricType.name() + ":" + signalMoment.strength + ", ";
			}

			listOfString.add(stringForSignal);

			if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit) {
				listOfString.add("$ " + new DecimalFormat("#.00").format(strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true)));
			} else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry) {
				listOfString.add("-");
			} else {
				listOfString.add("-");
			}

			listOfDisplayRows.add(listOfString);
		}
		
		return listOfDisplayRows;
	}
	
	public static class BacktestResultTransactionDetails {
		public int countForTradeEntry;
		public int countForTradeExit;
		public int countForTradesProfit;
		public int countForTradesLoss;
		public int countForTradesReentry;
		
		public double avgTradeLoss;
		public double avgTradeWin;
		
		public double minTradeWin;
		public double minTradeLoss;
		
		public double maxTradeWin;
		public double maxTradeLoss;
	}
}
