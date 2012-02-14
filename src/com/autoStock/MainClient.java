package com.autoStock;

import java.sql.SQLException;
import java.util.ArrayList;

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
		
		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
				BasicQueries.basic_historical_price_range,
				QueryArgs.symbol.setValue("RAS"),
				QueryArgs.startDate.setValue("2011-01-03 10:00:00"),
				QueryArgs.endDate.setValue("2011-01-04 14:00:00"));
		
		ArrayList<ArrayList<String>> listOfRows = new ArrayList<ArrayList<String>>();
		
		float lastPriceClose = 0;
		
		for (DbStockHistoricalPrice row : listOfResults){
			ArrayList<String> columns = new ReflectionHelper().getValuesToStringArryay(row);
			columns.add(StringTools.blankZeroValues(StringTools.addPlusToPositiveNumbers(MathTools.roundToTwoDecimalPlaces(row.priceClose - lastPriceClose))));
			listOfRows.add(columns);
			
			lastPriceClose = row.priceClose;
		}
		
		new TableController().displayTable(AsciiTables.stock_historical_price_db.injectColumns(AsciiColumns.derivedChange), listOfRows);
		
		Co.println("Size: " + listOfResults.size());
		
		System.exit(0);

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
