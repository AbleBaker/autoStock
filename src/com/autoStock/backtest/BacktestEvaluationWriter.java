package com.autoStock.backtest;

import java.text.DecimalFormat;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.internal.GsonClassAdapter;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.tools.DateTools;
import com.google.gson.GsonBuilder;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluationWriter {
	public void writeToDatabase(BacktestEvaluator backtestEvaluator, BacktestContainer backtestContainer, boolean includeOutOfSample){
		BacktestEvaluation bestEvaluation = backtestEvaluator.getResults(backtestContainer.symbol).get(backtestEvaluator.getResults(backtestContainer.symbol).size() -1);
		BacktestEvaluation outOfSampleEvaluation = null;
		
		if (includeOutOfSample){
			 outOfSampleEvaluation = new BacktestEvaluationBuilder().buildOutOfSampleEvaluation(backtestContainer, bestEvaluation);
		}
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(SignalParameters.class, new GsonClassAdapter());
		
		int gsonId = new DatabaseQuery().insert(BasicQueries.basic_insert_gson, new QueryArg(QueryArgs.gsonString, gsonBuilder.create().toJson(bestEvaluation).replace("\"", "\\\"")));
		
		new DatabaseQuery().insert(BasicQueries.basic_insert_backtest_results,
			new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(backtestContainer.historicalData.startDate)),
			new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(backtestContainer.historicalData.endDate)),
			new QueryArg(QueryArgs.runDate, DateTools.getSqlDate(new Date())),
			new QueryArg(QueryArgs.exchange, bestEvaluation.exchange.exchangeName),
			new QueryArg(QueryArgs.symbol, bestEvaluation.symbol.symbolName),
			new QueryArg(QueryArgs.balanceInBand, new DecimalFormat("#.00").format(bestEvaluation.accountBalance)),
			new QueryArg(QueryArgs.balanceOutBand, new DecimalFormat("#.00").format(outOfSampleEvaluation == null ? 0 : outOfSampleEvaluation.accountBalance)),
			new QueryArg(QueryArgs.percentGainInBand, "0"),
			new QueryArg(QueryArgs.percentGainOutBand, "0"),
			new QueryArg(QueryArgs.tradeEntry, String.valueOf(bestEvaluation.backtestResultTransactionDetails.countForTradeEntry)),
			new QueryArg(QueryArgs.tradeReentry, String.valueOf(bestEvaluation.backtestResultTransactionDetails.countForTradesReentry)),
			new QueryArg(QueryArgs.tradeExit, String.valueOf(bestEvaluation.backtestResultTransactionDetails.countForTradeExit)),
			new QueryArg(QueryArgs.tradeWins, String.valueOf(bestEvaluation.backtestResultTransactionDetails.countForTradesProfit)),
			new QueryArg(QueryArgs.tradeLoss, String.valueOf(bestEvaluation.backtestResultTransactionDetails.countForTradesLoss)),
			new QueryArg(QueryArgs.gsonId, String.valueOf(gsonId))
		);
		
		Co.println("--> Built OK: " + outOfSampleEvaluation.accountBalance);
	}
}
