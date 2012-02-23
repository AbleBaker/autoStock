package com.autoStock;

import java.sql.SQLException;
import java.util.ArrayList;

import org.jfree.data.time.TimeSeriesCollection;

import com.autoStock.algorithm.basic.AlgoDayOverDay;
import com.autoStock.analysis.AnalysisADX;
import com.autoStock.analysis.AnalysisBollingerBands;
import com.autoStock.analysis.AnalysisCCI;
import com.autoStock.analysis.AnalysisMACD;
import com.autoStock.analysis.results.ResultsADX;
import com.autoStock.analysis.results.ResultsBollingerBands;
import com.autoStock.analysis.results.ResultsCCI;
import com.autoStock.analysis.results.ResultsMACD;
import com.autoStock.chart.ChartDataFiller;
import com.autoStock.chart.CombinedLineChart;
import com.autoStock.chart.ChartDataFiller.BasicTimeValuePair;
import com.autoStock.chart.LineChart;
import com.autoStock.database.BuildDatabaseDefinitions;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.internal.Global.Mode;
import com.autoStock.menu.MenuController;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.menu.MenuDisplayLauncher;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiColumns;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.ReflectionHelper;
import com.autoStock.tools.StringTools;
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
		
		//new BuildDatabaseDefinitions().writeGeneratedJavaFiles();
		//new AlgoDayOverDay().simpleTest();
		//new TALibTest().test();
		
		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
				BasicQueries.basic_historical_price_range,
				QueryArgs.symbol.setValue("RAS"),
				QueryArgs.startDate.setValue("2011-01-05 09:30:00"),
				QueryArgs.endDate.setValue("2011-01-05 15:30:00"));
		
//		AnalysisBollingerBands analysis = new AnalysisBollingerBands();
//		analysis.setDataSet(listOfResults);
//		ResultsBollingerBands resultsBollingerBands = analysis.analyize(MAType.T3);
//		TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
//		timeSeriesCollection.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Lower", resultsBollingerBands.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfLowerBand)));
//		timeSeriesCollection.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Middle", resultsBollingerBands.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMiddleBand)));
//		timeSeriesCollection.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Upper", resultsBollingerBands.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfUpperBand)));
//		new LineChart(). new LineChartDisplay(timeSeriesCollection);

//		AnalysisCCI analysis = new AnalysisCCI();
//		analysis.setDataSet(listOfResults);
//		ResultsCCI resultsCommodityChannelIndex = analysis.analyize();
//		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
//		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("CCI", resultsCommodityChannelIndex.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfCCI)));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", resultsCommodityChannelIndex.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfPrice)));		
//		new CombinedLineChart().new LineChartDisplay(timeSeriesCollection1, timeSeriesCollection2);
		
//		AnalysisADX analysis = new AnalysisADX();
//		analysis.setDataSet(listOfResults);
//		ResultsADX resultsADX = analysis.analize();
//		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
//		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("ADX", resultsADX.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfADX)));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", resultsADX.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfPrice)));		
//		new CombinedLineChart().new LineChartDisplay(timeSeriesCollection1, timeSeriesCollection2);
		
		AnalysisMACD analysis = new AnalysisMACD();
		analysis.setDataSet(listOfResults);
		ResultsMACD resultsMACD = analysis.analize();
		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD ", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMACD)));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD Signal", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMACDSignal)));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD Histogram", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfMACDHistogram)));
		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", resultsMACD.getResultsAsListOfBasicTimeValuePair(analysis.results.arrayOfDates, analysis.results.arrayOfPrice)));		
		new CombinedLineChart().new LineChartDisplay(timeSeriesCollection1, timeSeriesCollection2);
		//new LineChart(). new LineChartDisplay(timeSeriesCollection1);
		
		//System.exit(0);

//	
//		MenuController menuController = new MenuController();
//		if (args.length == 0){menuController.displayMenu(MenuStructures.menu_main); ApplicationStates.shutdown();}
//		MenuStructures menuStructure = menuController.getRelatedMenu(args);
//		menuController.handleMenuStructure(menuStructure, args);
//		
//		new MenuDisplayLauncher().launchDisplay(menuStructure);
//		
//		Co.println("\n\nWaiting for callbacks...");
//		try{Thread.sleep(30*1000);}catch(Exception e){}
//		Co.println("\n Done \n");
//		System.exit(0);
		
	}
}
