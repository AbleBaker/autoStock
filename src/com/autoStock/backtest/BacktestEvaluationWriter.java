package com.autoStock.backtest;

import java.text.DecimalFormat;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.internal.GsonClassAdapter;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.tools.DateTools;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
import com.google.gson.GsonBuilder;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluationWriter {
	public void writeToDatabase(BacktestEvaluation backtestEvaluation, boolean includeOutOfSample){

		BacktestEvaluation outOfSampleEvaluation = null;
		
		if (includeOutOfSample){
			 outOfSampleEvaluation = new BacktestEvaluationBuilder().buildOutOfSampleEvaluation(backtestEvaluation);
		}
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(SignalParameters.class, new GsonClassAdapter());
		
		int gsonId = new DatabaseQuery().insert(BasicQueries.basic_insert_gson, new QueryArg(QueryArgs.gsonString, gsonBuilder.create().toJson(backtestEvaluation).replace("\"", "\\\"")));
		
		new DatabaseQuery().insert(BasicQueries.basic_insert_backtest_results,
			new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(backtestEvaluation.dateStart)),
			new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(backtestEvaluation.dateEnd)),
			new QueryArg(QueryArgs.runDate, DateTools.getSqlDate(new Date())),
			new QueryArg(QueryArgs.exchange, backtestEvaluation.exchange.exchangeName),
			new QueryArg(QueryArgs.symbol, backtestEvaluation.symbol.symbolName),
			new QueryArg(QueryArgs.balanceInBand, new DecimalFormat("#.00").format(backtestEvaluation.accountBalance)),
			new QueryArg(QueryArgs.balanceOutBand, new DecimalFormat("#.00").format(outOfSampleEvaluation == null ? 0 : outOfSampleEvaluation.accountBalance)),
			new QueryArg(QueryArgs.percentGainInBand, new DecimalFormat("#.00").format(backtestEvaluation.percentYield)),
			new QueryArg(QueryArgs.percentGainOutBand, "0"),
			new QueryArg(QueryArgs.tradeEntry, String.valueOf(backtestEvaluation.backtestResultTransactionDetails.countForTradeLongEntry + backtestEvaluation.backtestResultTransactionDetails.countForTradeShortEntry)),
			new QueryArg(QueryArgs.tradeReentry, String.valueOf(backtestEvaluation.backtestResultTransactionDetails.countForTradesReentry)),
			new QueryArg(QueryArgs.tradeExit, String.valueOf(backtestEvaluation.backtestResultTransactionDetails.countForTradeExit)),
			new QueryArg(QueryArgs.tradeWins, String.valueOf(backtestEvaluation.backtestResultTransactionDetails.countForTradesProfit)),
			new QueryArg(QueryArgs.tradeLoss, String.valueOf(backtestEvaluation.backtestResultTransactionDetails.countForTradesLoss)),
			new QueryArg(QueryArgs.gsonId, String.valueOf(gsonId))
		);
	}
}
