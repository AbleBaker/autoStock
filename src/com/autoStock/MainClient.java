package com.autoStock;

import java.sql.SQLException;
import java.util.ArrayList;

import org.jfree.data.time.TimeSeriesCollection;

import com.autoStock.algorithm.basic.AlgoDayOverDay;
import com.autoStock.analysis.AnalysisBollingerBands;
import com.autoStock.analysis.TALibTest;
import com.autoStock.analysis.results.ResultsBollingerBands;
import com.autoStock.chart.ChartDataFiller;
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
				QueryArgs.symbol.setValue("BTU"),
				QueryArgs.startDate.setValue("2011-01-03 09:30:00"),
				QueryArgs.endDate.setValue("2011-01-03 15:50:00"));
		
		AnalysisBollingerBands analysis = new AnalysisBollingerBands();
		analysis.setDataSet(listOfResults);
		ResultsBollingerBands resultsBollingerBands = analysis.analyize(MAType.Kama);
		
		TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
		timeSeriesCollection.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Upper", resultsBollingerBands.getResultsAsListOfBasicTimeValuePair(analysis.dataExtractor.resultsOfDate, analysis.results.arrayOfUpperBand)));
		timeSeriesCollection.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Middle", resultsBollingerBands.getResultsAsListOfBasicTimeValuePair(analysis.dataExtractor.resultsOfDate, analysis.results.arrayOfMiddleBand)));
		timeSeriesCollection.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Lower", resultsBollingerBands.getResultsAsListOfBasicTimeValuePair(analysis.dataExtractor.resultsOfDate, analysis.results.arrayOfLowerBand)));
		
		new LineChart().new LineChartDisplay(timeSeriesCollection);
		
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
