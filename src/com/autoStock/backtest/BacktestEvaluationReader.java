/**
 * 
 */
package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbGson;
import com.autoStock.internal.GsonClassAdapter;
import com.autoStock.signal.SignalDefinitions.IndicatorParameters;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
import com.google.gson.GsonBuilder;

/**
 * @author Kevin
 *
 */
public class BacktestEvaluationReader {	
	public static BacktestEvaluation getPrecomputedEvaluation(Exchange exchange, Symbol symbol){
		ArrayList<DbGson> listOfGsonResults = (ArrayList<DbGson>) new DatabaseQuery().getQueryResults(BasicQueries.basic_get_backtest_evaluation, new QueryArg(QueryArgs.symbol, symbol.symbolName), new QueryArg(QueryArgs.exchange, exchange.exchangeName));
		
		if (listOfGsonResults.size() > 0){
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(SignalParameters.class, new GsonClassAdapter());
			gsonBuilder.registerTypeAdapter(IndicatorParameters.class, new GsonClassAdapter());
			
			BacktestEvaluation backtestEvaluation = gsonBuilder.create().fromJson(listOfGsonResults.get(0).gson, BacktestEvaluation.class);				
			return backtestEvaluation;
		}
		return null;
	}
	
	public static AlgorithmModel getPrecomputedModel(Exchange exchange, Symbol symbol){
		BacktestEvaluation backtestEvaluation = getPrecomputedEvaluation(exchange, symbol);
		return backtestEvaluation == null ? null : backtestEvaluation.algorithmModel;
	}
}
