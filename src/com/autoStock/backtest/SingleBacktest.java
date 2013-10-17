package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.core.AlgorithmRemodeler;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.Lock;
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
	
	private Lock lock = new Lock();
	
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
//		Co.println("--> Backtest completed... " + symbol.symbolName + ", " + currentBacktestDayIndex);
		currentBacktestDayIndex++;
		
		if (currentBacktestDayIndex == listOfHistoricalDataList.size()) {
			synchronized(lock){lock.isLocked = false; try {lock.notify();}catch(Exception e){}}
		}else{
			selfPopulateBacktestData();
			runBacktest();
		}
	}
	
	public void selfPopulateBacktestData(){
		HistoricalData historicalData = BacktestUtils.getHistoricalDataForSymbol(listOfHistoricalDataList.get(currentBacktestDayIndex), backtestContainer.symbol.symbolName);
		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.symbolName), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));
		
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
		backtestContainer.runBacktest();
		if (lock.isLocked == false){synchronized(lock){try {lock.isLocked = true; lock.wait();}catch(Exception e){e.printStackTrace();}}}
	}

	public void remodel(AlgorithmModel algorithmModel) {
		new AlgorithmRemodeler(backtestContainer.algorithm, algorithmModel).remodel();
	}
}
