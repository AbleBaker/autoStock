package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.account.AccountProvider;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class SingleBacktest implements ListenerOfBacktestCompleted {
	private BacktestContainer backtestContainer;
	private HistoricalData historicalData;
	
	public SingleBacktest(HistoricalData historicalData){
		ArrayList<Date> listOfBacktestDates = DateTools.getListOfDatesOnWeekdays(historicalData.startDate, historicalData.endDate);

		if (listOfBacktestDates.size() == 0) {
			throw new IllegalArgumentException("Weekday not entered");
		}
		
		this.historicalData = historicalData;
		backtestContainer = new BacktestContainer(historicalData.symbol, historicalData.exchange, this, AlgorithmMode.mode_backtest_silent);
	}

	@Override
	public void backtestCompleted(Symbol symbol, AlgorithmBase algorithmBase) {
		
	}
	
	public void setBacktestData(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice){
		if (listOfDbStockHistoricalPrice.size() == 0){
			throw new IllegalStateException("Backtest data size cannot be zero");
		}
		backtestContainer.setBacktestData(listOfDbStockHistoricalPrice, historicalData);
	}

	public void runBacktest() {
		backtestContainer.runBacktest();
	}
}
