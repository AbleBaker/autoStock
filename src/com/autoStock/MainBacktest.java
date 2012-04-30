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

		typeHistoricalData.startDate.setDate(3);
		typeHistoricalData.endDate.setDate(3);
		runBacktest(typeHistoricalData);
		
		typeHistoricalData.startDate.setDate(4);
		typeHistoricalData.endDate.setDate(4);
		runBacktest(typeHistoricalData);
		
		typeHistoricalData.startDate.setDate(5);
		typeHistoricalData.endDate.setDate(5);
		runBacktest(typeHistoricalData);
		
		typeHistoricalData.startDate.setDate(6);
		typeHistoricalData.endDate.setDate(6);
		runBacktest(typeHistoricalData);
		
		typeHistoricalData.startDate.setDate(7);
		typeHistoricalData.endDate.setDate(7);
		runBacktest(typeHistoricalData);
	
		typeHistoricalData.startDate.setDate(8);
		typeHistoricalData.endDate.setDate(8);
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
