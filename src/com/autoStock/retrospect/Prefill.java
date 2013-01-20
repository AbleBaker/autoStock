package com.autoStock.retrospect;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class Prefill {
	private Symbol symbol;
	private Exchange exchange;
	private PrefillMethod prefillMethod;
	
	public static enum PrefillMethod {
		method_database,
		method_broker,
	}
	
	public Prefill(Symbol symbol, Exchange exchange, PrefillMethod prefillMethod){
		this.symbol = symbol;
		this.exchange = exchange;
		this.prefillMethod = prefillMethod;
	}
	
	public void prefillAlgorithm(AlgorithmBase algorithmBase, Date fromDate, Date toDate){
		if (prefillMethod == PrefillMethod.method_database){
			HistoricalData historicalData = new HistoricalData(exchange, symbol, fromDate, toDate, Resolution.min);
			ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, QueryArgs.symbol.setValue(historicalData.symbol.symbolName), QueryArgs.startDate.setValue(DateTools.getSqlDate(historicalData.startDate)), QueryArgs.endDate.setValue(DateTools.getSqlDate(historicalData.endDate)));

		}
	}
}
