package com.autoStock.backtest.watchmaker;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.adjust.AdjustmentOfSignalMetricThreshold;
import com.autoStock.adjust.IterableOfInteger;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentType;
import com.autoStock.backtest.BacktestEvaluator;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.indicator.results.ResultsSAR;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.HistoricalDataList;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMBacktest {
	private Exchange exchange;
	private ArrayList<Symbol> listOfSymbols;
	public final BacktestEvaluator backtestEvaluator = new BacktestEvaluator();
	private ArrayList<WMBacktestContainer> listOfWMBacktestContainer = new ArrayList<WMBacktestContainer>();
	private ArrayList<HistoricalDataList> listOfHistoricalDataList = new ArrayList<HistoricalDataList>();
	private Date dateStart;
	private Date dateEnd;
	private int currentDayIndex = 0;
	private Benchmark bench = new Benchmark();
	
	public WMBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<Symbol> listOfSymbols) {
		this.exchange = exchange;
		this.listOfSymbols = listOfSymbols;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		
		Global.callbackLock.requestLock();
		
		if (listOfSymbols.size() == 0){
			throw new IllegalArgumentException("No symbols specified");
		}
		
		ListTools.removeDuplicates(listOfSymbols);
		listOfHistoricalDataList = BacktestUtils.getHistoricalDataList(exchange, dateStart, dateEnd, listOfSymbols);
		
		bench.printTick("Started");
		
		setupBacktestContainers();
		runBacktest();
		
		bench.printTick("Ended");
		Global.callbackLock.releaseLock();
	}
	
	private void setupBacktestContainers(){
		for (Symbol symbol : listOfSymbols){
			HistoricalData historicalData = new HistoricalData(exchange, symbol, dateStart, dateEnd, null);
			
			WMBacktestContainer wmBacktestContainer = new WMBacktestContainer(symbol, exchange, dateStart, dateEnd);
			
			listOfWMBacktestContainer.add(wmBacktestContainer);
		}
	}
	
	public void runBacktest(){
		for (WMBacktestContainer wmBacktestContainer : listOfWMBacktestContainer){
			wmBacktestContainer.runBacktest();
		}
	}
}
