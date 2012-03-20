package com.autoStock;

import java.sql.SQLException;
import java.util.ArrayList;

import org.jfree.data.time.TimeSeriesCollection;

import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.algorithm.basic.AlgoDayOverDay;
import com.autoStock.analysis.AnalysisADX;
import com.autoStock.analysis.AnalysisBB;
import com.autoStock.analysis.AnalysisCCI;
import com.autoStock.analysis.AnalysisMACD;
import com.autoStock.analysis.AnalysisRSI;
import com.autoStock.analysis.results.ResultsADX;
import com.autoStock.analysis.results.ResultsBB;
import com.autoStock.analysis.results.ResultsCCI;
import com.autoStock.analysis.results.ResultsMACD;
import com.autoStock.analysis.results.ResultsRSI;
import com.autoStock.backtest.Backtest;
import com.autoStock.chart.ChartDataFiller;
import com.autoStock.chart.CombinedLineChart;
import com.autoStock.chart.LineChart;
import com.autoStock.database.BuildDatabaseDefinitions;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.RequestMarketData;
import com.autoStock.exchange.request.RequestMarketOrder;
import com.autoStock.exchange.request.RequestMarketScanner;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.internal.Global.Mode;
import com.autoStock.menu.MenuController;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.menu.MenuDisplayLauncher;
import com.autoStock.scanner.MarketScanner;
import com.autoStock.scanner.Shortlist;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiColumns;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.ReflectionHelper;
import com.autoStock.tools.StringTools;
import com.autoStock.trading.platform.ib.IbExchangeInstance;
import com.autoStock.trading.platform.ib.definitions.HistoricalData.Resolution;
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.trading.types.TypeMarketData;
import com.autoStock.trading.types.TypePosition;
import com.autoStock.types.ConvertTypes;
import com.autoStock.types.TypeQuoteSlice;
import com.tictactec.ta.lib.MAType;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClient {	
	public static void main(String[] args) throws SQLException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Global.mode = Mode.client;
		Co.println("Welcome to autoStock\n");
			
		ApplicationStates.startup();
//		
//		IbExchangeInstance ibExchangeInstance = ExchangeController.getIbExchangeInstance();
//		TypePosition typePosition = new TypePosition(1200, "BTU", "STK", 0);
//		Signal signal = new Signal(SignalSource.from_manual);
//		signal.currentSignalType = SignalType.type_buy;
//		
//		new RequestMarketOrder(new RequestHolder(null), typePosition, signal);
		
		//ibExchangeInstance.placeSellOrder(typePosition, new RequestHolder(null));
		
		//ibExchangeInstance.getAccountUpdates();
//		ibExchangeInstance.getOpenOrders();
		
		//ExchangeController.getIbExchangeInstance().ibExchangeClientSocket.eClientSocket.reqScannerParameters();
		
		new RequestMarketScanner(new RequestHolder(null));
		
//		TypeMarketData typeMarketData = new TypeMarketData("8411", "STK");
//		
//		new RequestMarketData(new RequestHolder(null), new RequestMarketDataListener() {
//			@Override
//			public void failed(RequestHolder requestHolder) {
//				
//			}
//			
//			@Override
//			public void completed(RequestHolder requestHolder, ExResultSetMarketData exResultSetMarketData) {
//				Co.println("Completed!");
//				
//			}
//		}, typeMarketData, 5000);
		
		//new BuildDatabaseDefinitions().writeGeneratedJavaFiles();
		//new AlgoDayOverDay().simpleTest();
		//new TALibTest().test();
		
//		MarketScanner marketScanner = new MarketScanner();
//		marketScanner.startScan();
		
		//TypeHistoricalData typeHistoricalData = new TypeHistoricalData("BTU", "STK", DateTools.getDateFromString("2011-01-05 09:30:00"), DateTools.getDateFromString("2011-01-05 16:00:00"), Resolution.min);		
//		TypeHistoricalData typeHistoricalData = new TypeHistoricalData("BTU", "STK", DateTools.getDateFromString("2011-01-13 09:30:00"), DateTools.getDateFromString("2011-01-13 16:00:00"), Resolution.min);
//
//		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
//				BasicQueries.basic_historical_price_range,
//				QueryArgs.symbol.setValue(typeHistoricalData.symbol),
//				QueryArgs.startDate.setValue(DateTools.getSqlDate(typeHistoricalData.startDate)),
//				QueryArgs.endDate.setValue(DateTools.getSqlDate(typeHistoricalData.endDate)));
//				
//		Backtest backtest = new Backtest(typeHistoricalData, listOfResults);
//		AlgorithmTest algorithm = new AlgorithmTest();
//		
//		backtest.performBacktest(algorithm.getReceiver());
		
//		AnalysisMACD analysis = new AnalysisMACD(30, true);
//		analysis.setDataSet(new ConvertTypes().convertToQuoteSlice(listOfResults));
//		ResultsMACD resultsMACD = analysis.analize();
//		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
//		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal PPC ", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMACD)));
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal ADX", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMACDSignal)));
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal CCI", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMACDHistogram)));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfPrice)));		
//		new CombinedLineChart().new LineChartDisplay(timeSeriesCollection1, timeSeriesCollection2);
////		//new LineChart(). new LineChartDisplay(timeSeriesCollection1);
		
//		AnalysisBB analysis = new AnalysisBB(30, false);
//		analysis.setDataSetFromDatabase(listOfResults);
//		ResultsBB resultsBollingerBands = analysis.analyize(MAType.T3);
//		TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
//		timeSeriesCollection.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Lower", resultsBollingerBands.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfLowerBand)));
//		timeSeriesCollection.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Middle", resultsBollingerBands.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMiddleBand)));
//		timeSeriesCollection.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Upper", resultsBollingerBands.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfUpperBand)));
//		new LineChart(). new LineChartDisplay(timeSeriesCollection);

//		AnalysisCCI analysis = new AnalysisCCI(30,false);
//		analysis.setDataSetFromDatabase(listOfResults);
//		ResultsCCI resultsCommodityChannelIndex = analysis.analyize();
//		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
//		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("CCI", resultsCommodityChannelIndex.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, ArrayTools.shiftArrayDown(analysis.results.arrayOfCCI, analysis.periodLength))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", resultsCommodityChannelIndex.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfPrice)));		
//		new CombinedLineChart().new LineChartDisplay(timeSeriesCollection1, timeSeriesCollection2);
		
//		AnalysisADX analysis = new AnalysisADX(30, false);
//		analysis.setDataSet(new ConvertTypes().convertToQuoteSlice(listOfResults));
//		ResultsADX resultsADX = analysis.analize();
//		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
//		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("ADX", resultsADX.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfADX)));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", resultsADX.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfPrice)));		
//		new CombinedLineChart().new LineChartDisplay(timeSeriesCollection1, timeSeriesCollection2);
		
//		AnalysisMACD analysis = new AnalysisMACD(128, true);
//		analysis.setDataSet(new ConvertTypes().convertToQuoteSlice(listOfResults));
//		ResultsMACD resultsMACD = analysis.analize();
//		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
//		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD ", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMACD)));
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD Signal", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMACDSignal)));
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD Histogram", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMACDHistogram)));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfPrice)));		
//		new CombinedLineChart().new LineChartDisplay(timeSeriesCollection1, timeSeriesCollection2);
////		//new LineChart(). new LineChartDisplay(timeSeriesCollection1);
		
//		AnalysisRSI analysis = new AnalysisRSI(30, true);
//		analysis.setDataSet(new ConvertTypes().convertToQuoteSlice(listOfResults));
//		ResultsRSI resultsRSI = analysis.analize();
//		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
//		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("RSI", resultsRSI.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfRSI)));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", resultsRSI.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfPrice)));		
//		new CombinedLineChart().new LineChartDisplay(timeSeriesCollection1, timeSeriesCollection2);
		
		//System.exit(0);

//	
//		MenuController menuController = new MenuController();
//		if (args.length == 0){menuController.displayMenu(MenuStructures.menu_main); ApplicationStates.shutdown();}
//		MenuStructures menuStructure = menuController.getRelatedMenu(args);
//		menuController.handleMenuStructure(menuStructure, args);
//		
//		new MenuDisplayLauncher().launchDisplay(menuStructure);
////		
//		Co.println("\n\nWaiting for callbacks...");
//		try{Thread.sleep(15*1000);}catch(Exception e){}
//		Co.println("\n Done \n");
//		System.exit(0);
		
	}
}
