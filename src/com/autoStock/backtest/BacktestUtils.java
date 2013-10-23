package com.autoStock.backtest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.account.BasicAccount;
import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentCampaignProvider;
import com.autoStock.adjust.AdjustmentIdentifier;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfEnum;
import com.autoStock.adjust.AdjustmentOfSignalMetricThreshold;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.guage.SignalGuage;
import com.autoStock.indicator.IndicatorBase;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalMoment;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.strategy.StrategyResponse.StrategyAction;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.MiscTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.HistoricalDataList;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
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
		
		string += "\n --> Entered (Long / Short), reentered, exited: " + + backtestResultDetails.countForTradeLongEntry + " / " + backtestResultDetails.countForTradeShortEntry  + ", " + backtestResultDetails.countForTradesReentry + ", " + backtestResultDetails.countForTradeExit;
		string += "\n --> Transactions profit / loss: " + MathTools.round(((double)backtestResultDetails.countForTradesProfit / (double)(backtestResultDetails.countForTradesProfit + backtestResultDetails.countForTradesLoss)) * 100) + "%, " + backtestResultDetails.countForTradesProfit + ", " + backtestResultDetails.countForTradesLoss;
		
		if (basicAccount.getTransactions() > 0 && backtestResultDetails.countForTradesProfit == 0 && backtestResultDetails.countForTradesLoss == 0){
			throw new IllegalStateException("Details: " + basicAccount.getTransactions() + ", " + backtestResultDetails.countForTradesProfit + ", " + backtestResultDetails.countForTradesLoss);
		}
		
		for (SignalBase signalBase : signal.getListOfSignalBase()){
			string += "\n\n --> Signal metric: " + signalBase.signalMetricType.name() + "\n";
			
			if (signalBase.signalParameters.arrayOfSignalGuageForLongEntry != null){
				for (SignalGuage signalGuage : signalBase.signalParameters.arrayOfSignalGuageForLongEntry){
					string += " +Long entry: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.mutableEnumForSignalGuageType.enumValue.name() + "\n";
				}
			}
			
			if (signalBase.signalParameters.arrayOfSignalGuageForLongExit != null){
				for (SignalGuage signalGuage : signalBase.signalParameters.arrayOfSignalGuageForLongExit){
					string += " +Long exit: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.mutableEnumForSignalGuageType.enumValue.name() + "\n";
				}
			}
			
			if (signalBase.signalParameters.arrayOfSignalGuageForShortEntry != null){
				for (SignalGuage signalGuage : signalBase.signalParameters.arrayOfSignalGuageForShortEntry){
					string += " +Short entry: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.mutableEnumForSignalGuageType.enumValue.name() + "\n";
				}
			}
			
			if (signalBase.signalParameters.arrayOfSignalGuageForShortExit != null){
				for (SignalGuage signalGuage : signalBase.signalParameters.arrayOfSignalGuageForShortExit){
					string += " +Short exit: " + signalGuage.threshold + ", " + signalGuage.signalBounds.name() + ", " + signalGuage.mutableEnumForSignalGuageType.enumValue.name() + "\n";
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
					}else if (adjustmentBase instanceof AdjustmentOfSignalMetricThreshold){
						string += " +AdjustmentOfSignalMetric " + adjustmentBase.getDescription() + " : " + ((AdjustmentOfSignalMetricThreshold)adjustmentBase).getValue() + "\n";
					}
			}
		}
		
		string += "\n";
		
		string += strategyOptions.toString();
		
		string += "\n\n";
		
		return string;
	}
	
	public static void printBestBacktestResults(ArrayList<String> listOfStringBestBacktestResults){
		if (listOfStringBestBacktestResults!= null && listOfStringBestBacktestResults.size() > 0){
			Co.println("Best backtest results...");
			for (String string : listOfStringBestBacktestResults){
				Co.println(string);
			}
		}
	}
	
	public static BacktestResultTransactionDetails getProfitLossDetails(ArrayList<BacktestContainer> listOfBacktestContainer){
		BacktestResultTransactionDetails backtestProfitLossType = new BacktestResultTransactionDetails();
		
		for (BacktestContainer backtestContainer : listOfBacktestContainer){
			BacktestResultTransactionDetails backtestTransactions = getBacktestResultTransactionDetails(backtestContainer);
			
			backtestProfitLossType.countForTradeLongEntry += backtestTransactions.countForTradeLongEntry;
			backtestProfitLossType.countForTradeShortEntry += backtestTransactions.countForTradeShortEntry;
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
				
				double transactionProfit = strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true);
				
				if (transactionProfit > 0){
					backtestTransactions.countForTradesProfit++;
					backtestTransactions.avgTradeWin += transactionProfit;
					if (transactionProfit > backtestTransactions.maxTradeWin){backtestTransactions.maxTradeWin = transactionProfit;}
					if (transactionProfit < backtestTransactions.minTradeWin || backtestTransactions.minTradeWin == 0){backtestTransactions.minTradeWin = transactionProfit;}
				}else if (transactionProfit <= 0){
					backtestTransactions.countForTradesLoss++;
					backtestTransactions.avgTradeLoss += transactionProfit;
					
					if (transactionProfit < backtestTransactions.maxTradeLoss){backtestTransactions.maxTradeLoss = transactionProfit;}
					if (transactionProfit > backtestTransactions.minTradeLoss || backtestTransactions.minTradeLoss == 0){backtestTransactions.minTradeLoss = transactionProfit;}
				}
				
				backtestTransactions.countForTradeExit++;
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry){
				backtestTransactions.countForTradeLongEntry++;
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry){
				backtestTransactions.countForTradeShortEntry++;
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry){
				backtestTransactions.countForTradesReentry++;
			}
		}
		
		if (backtestTransactions.countForTradesProfit > 0){backtestTransactions.avgTradeWin /= backtestTransactions.countForTradesProfit;}
		if (backtestTransactions.countForTradesLoss > 0){backtestTransactions.avgTradeLoss /= backtestTransactions.countForTradesLoss;}
		
		return backtestTransactions;
	}
	
	public static ArrayList<ArrayList<String>> getTableDisplayRows(BacktestContainer backtestContainer){
		ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();

		for (StrategyResponse strategyResponse : backtestContainer.listOfStrategyResponse) {
			ArrayList<String> listOfString = new ArrayList<String>();
			listOfString.add(DateTools.getPrettyDate(strategyResponse.quoteSlice.dateTime));
			listOfString.add(backtestContainer.symbol.symbolName);
			listOfString.add(new DecimalFormat("#.00").format(strategyResponse.quoteSlice.priceClose));
			listOfString.add(strategyResponse.strategyActionCause.name().replaceAll("changed", "").replaceAll("proceed_", "..."));
			listOfString.add(strategyResponse.positionGovernorResponse.status.name().replaceAll("changed_", ""));

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
	
	public static BacktestContainer getBacktestContainerForSymbol(Symbol symbol, ArrayList<BacktestContainer> listOfBacktestContainer){
		for (BacktestContainer backtestContainer : listOfBacktestContainer){
			if (backtestContainer.symbol.equals(symbol)){
				return backtestContainer;
			}
		}
		return null;
	}
	
	public static ArrayList<HistoricalDataList> getHistoricalDataList(Exchange exchange, Date dateStart, Date dateEnd, List<Symbol> listOfSymbols){
		ArrayList<HistoricalDataList> listOfHistoricalDataList = new ArrayList<HistoricalDataList>();
		
		HistoricalData baseHistoricalData = getBaseHistoricalData(exchange, null, dateStart, dateEnd, Resolution.min);

		ArrayList<Date> listOfBacktestDates = DateTools.getListOfDatesOnWeekdays(baseHistoricalData.startDate, baseHistoricalData.endDate);

		if (listOfBacktestDates.size() == 0) {
			throw new IllegalArgumentException("Weekday not entered. Backtest must contain a weekday.");
		}

		for (Date date : listOfBacktestDates) {
			HistoricalDataList historicalDataList = new HistoricalDataList();

			for (Symbol symbol : listOfSymbols) {
				HistoricalData dayHistoricalData = new HistoricalData(exchange, symbol, (Date) date.clone(), (Date) date.clone(), baseHistoricalData.resolution);
				dayHistoricalData.startDate.setHours(exchange.timeOpenForeign.hours);
				dayHistoricalData.startDate.setMinutes(exchange.timeOpenForeign.minutes);
				dayHistoricalData.endDate.setHours(exchange.timeCloseForeign.hours);
				dayHistoricalData.endDate.setMinutes(exchange.timeCloseForeign.minutes);
				
				historicalDataList.listOfHistoricalData.add(dayHistoricalData);
			}

			listOfHistoricalDataList.add(historicalDataList);
		}
		
		return listOfHistoricalDataList;
	}
	
	public static HistoricalData getBaseHistoricalData(Exchange exchange, Symbol symbol, Date dateStart, Date dateEnd, Resolution resolution){
		HistoricalData baseHistoricalData = new HistoricalData(exchange, symbol, dateStart, dateEnd, resolution);

		baseHistoricalData.startDate.setHours(exchange.timeOpenForeign.hours);
		baseHistoricalData.startDate.setMinutes(exchange.timeOpenForeign.minutes);
		baseHistoricalData.endDate.setHours(exchange.timeCloseForeign.hours);
		baseHistoricalData.endDate.setMinutes(exchange.timeCloseForeign.minutes);
		
		return baseHistoricalData;
	}
	
	public static HistoricalData getHistoricalDataForSymbol(HistoricalDataList historicalDataList, String symbol) {
		if (historicalDataList.listOfHistoricalData.size() == 0) {
			throw new IllegalStateException("Historical data list size is 0 for symbol: " + symbol);
		}
		for (HistoricalData historicalData : historicalDataList.listOfHistoricalData) {
			if (historicalData.symbol.symbolName.equals(symbol)) {
				return historicalData;
			}
		}

		throw new IllegalStateException("No symbol data found for symbol: " + symbol);
	}
	
	public static class BacktestDayResultDetails {
		public Date dateStart;
		public Date dateEnd;
		public double percentYield;
		
		public BacktestDayResultDetails(Date dateStart, Date dateEnd) {
			this.dateStart = dateStart;
			this.dateEnd = dateEnd;
		}
	}
	
	public static class BacktestResultTransactionDetails {
		public int countForTradeLongEntry;
		public int countForTradeShortEntry;
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
