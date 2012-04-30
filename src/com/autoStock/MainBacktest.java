package com.autoStock;

import java.util.ArrayList;

import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.backtest.Backtest;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.exchange.results.ExResultHistoricalData;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.Global;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.types.TypeExchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainBacktest {
	private TypeHistoricalData typeHistoricalData;
	private ExResultHistoricalData exResultHistoricalData;
	
	public MainBacktest(TypeExchange exchange, TypeHistoricalData typeHistoricalData){
		this.typeHistoricalData = typeHistoricalData;
		Global.callbackLock.requestCallbackLock();
		
		typeHistoricalData.startDate.setHours(exchange.timeOpen.hour);
		typeHistoricalData.startDate.setMinutes(exchange.timeOpen.minute);
		typeHistoricalData.endDate.setHours(exchange.timeClose.hour);
		typeHistoricalData.endDate.setMinutes(exchange.timeClose.minute);
		typeHistoricalData.symbol = "BTU";
		
		Co.println("Running backtest on Exchange: " + exchange.exchange);
		Co.println("Running backtest for dates between " + DateTools.getPrettyDate(typeHistoricalData.startDate) + " - " + DateTools.getPrettyDate(typeHistoricalData.endDate));
		
		int days = typeHistoricalData.endDate.getDay() - typeHistoricalData.startDate.getDay();

		runBacktest(typeHistoricalData);
	}
	
	@SuppressWarnings("unchecked")
	public void runBacktest(TypeHistoricalData typeHistoricalData){
		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
				BasicQueries.basic_historical_price_range,
				QueryArgs.symbol.setValue(typeHistoricalData.symbol),
				QueryArgs.startDate.setValue(DateTools.getSqlDate(typeHistoricalData.startDate)),
				QueryArgs.endDate.setValue(DateTools.getSqlDate(typeHistoricalData.endDate)));

		Backtest backtest = new Backtest(typeHistoricalData, listOfResults);
		AlgorithmTest algorithm = new AlgorithmTest(false);
		backtest.performBacktest(algorithm.getReceiver());
	}
}
