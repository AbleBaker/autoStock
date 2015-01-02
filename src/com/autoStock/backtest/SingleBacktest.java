package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.core.AlgorithmRemodeler;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.HistoricalDataList;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class SingleBacktest implements ListenerOfBacktestCompleted {
	public BacktestContainer backtestContainer;
	private HistoricalData historicalData;
	private ArrayList<HistoricalDataList> listOfHistoricalDataList = new ArrayList<HistoricalDataList>();
	private int currentBacktestDayIndex = 0;
	private Benchmark bench = new Benchmark();
	
	public SingleBacktest(HistoricalData historicalData, AlgorithmMode algorithmMode){
		ArrayList<Date> listOfBacktestDates = DateTools.getListOfDatesOnWeekdays(historicalData.startDate, historicalData.endDate);

		if (listOfBacktestDates.size() == 0) {
			throw new IllegalArgumentException("Weekday not entered: " + historicalData.startDate + ", " + historicalData.endDate);
		}
		
		listOfHistoricalDataList = BacktestUtils.getHistoricalDataList(historicalData.exchange, historicalData.startDate, historicalData.endDate, Arrays.asList(new Symbol[]{historicalData.symbol}));
		
		this.historicalData = historicalData;
		backtestContainer = new BacktestContainer(historicalData.symbol, historicalData.exchange, this, algorithmMode);
	}

	@Override
	public void backtestCompleted(Symbol symbol, AlgorithmBase algorithmBase) {
		//Co.println("--> Backtest completed... " + symbol.symbolName + ", " + currentBacktestDayIndex);
		
		currentBacktestDayIndex++;
		
		if (currentBacktestDayIndex == listOfHistoricalDataList.size()) {
			//pass, used to release lock
			backtestContainer.markAsComplete();
		}else{
			selfPopulateBacktestData();
			runBacktest();
		}
	}
	
	public void selfPopulateBacktestData(){
		HistoricalData historicalData = BacktestUtils.getHistoricalDataForSymbol(listOfHistoricalDataList.get(currentBacktestDayIndex), backtestContainer.symbol.symbolName);
		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.symbolName), new QueryArg(QueryArgs.exchange, historicalData.exchange.exchangeName), new QueryArg(QueryArgs.resolution, historicalData.resolution.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));
		
		if (listOfResults.size() == 0){
			Co.println("--> Warning! No backtest data for symbol: " + historicalData.symbol.symbolName + " on " + historicalData.startDate + " to " + historicalData.endDate);
		}
		
		backtestContainer.setBacktestData(listOfResults, historicalData);
	}
	
	public void setBacktestData(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice){
		if (listOfDbStockHistoricalPrice.size() == 0){
			throw new IllegalStateException("Backtest data size cannot be zero");
		}
		backtestContainer.setBacktestData(listOfDbStockHistoricalPrice, historicalData);
	}

	public void runBacktest() {
		backtestContainer.prepare();
		backtestContainer.perform(true);
	}

	public void remodel(AlgorithmModel algorithmModel) {
		new AlgorithmRemodeler(backtestContainer.algorithm, algorithmModel).remodel();
	}
}
