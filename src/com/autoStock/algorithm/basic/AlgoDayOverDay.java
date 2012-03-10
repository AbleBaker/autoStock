/**
 * 
 */
package com.autoStock.algorithm.basic;

import java.util.ArrayList;
import java.util.Collections;

import com.autoStock.Co;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiColumns;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.ReflectionHelper;
import com.autoStock.tools.ReflectiveComparator;
import com.autoStock.tools.StringTools;
import com.autoStock.tools.ReflectiveComparator.ListComparator;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgoDayOverDay {
	@SuppressWarnings("unchecked")
	public void simpleTest(){
		ArrayList<DbStockHistoricalPrice> listOfResultsYesterdaysClose = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
			BasicQueries.basic_single_date_sample_all_stocks,
			QueryArgs.startDate.setValue("2011-01-03 15:59:00"),
			QueryArgs.endDate.setValue("2011-01-03 15:59:00"));
		
		ArrayList<DbStockHistoricalPrice> listOfResultsTodaysOpen = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
			BasicQueries.basic_single_date_sample_all_stocks,
			QueryArgs.startDate.setValue("2011-01-04 09:30:00"),
			QueryArgs.endDate.setValue("2011-01-04 09:30:00"));
		
		ArrayList<DbStockHistoricalPrice> listOfResultsTodaysClose = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
			BasicQueries.basic_single_date_sample_all_stocks,
			QueryArgs.startDate.setValue("2011-01-04 15:59:00:00"),
			QueryArgs.endDate.setValue("2011-01-04 15:59:00:00"));
		
		Co.println("Sizes: " +listOfResultsYesterdaysClose.size() + "," + listOfResultsTodaysOpen.size() + "," + listOfResultsTodaysClose.size());
		
		ArrayList<DbStockHistoricalPriceChange> listOfChanges = new ArrayList<DbStockHistoricalPriceChange>();
		
		for (DbStockHistoricalPrice stock : listOfResultsYesterdaysClose){
			DbStockHistoricalPrice todaysOpenStock = getDbStockHistoricalPrice(listOfResultsTodaysOpen, stock.symbol);
			if (todaysOpenStock != null){
				listOfChanges.add(new DbStockHistoricalPriceChange(todaysOpenStock, ((todaysOpenStock.priceClose - stock.priceClose)) / todaysOpenStock.priceClose));
			}
		}
		
		Collections.sort(listOfChanges, new ReflectiveComparator(). new ListComparator("changeInPriceClose"));
		listOfChanges = new ArrayList<DbStockHistoricalPriceChange>(listOfChanges.subList(listOfChanges.size()-10, listOfChanges.size()));
		
		for (DbStockHistoricalPriceChange changedStock : listOfChanges){
			Co.println("Change A (%): " + changedStock.dbHistoricalPrice.symbol + " -> " + changedStock.changeInPriceClose);
		}
		
		ArrayList<DbStockHistoricalPriceChange> listOfChangesAgain = new ArrayList<DbStockHistoricalPriceChange>();
		
		for (DbStockHistoricalPriceChange stock : listOfChanges){
			DbStockHistoricalPrice todaysCloseStock = getDbStockHistoricalPrice(listOfResultsTodaysClose, stock.dbHistoricalPrice.symbol);
			if (todaysCloseStock != null){
				listOfChangesAgain.add(new DbStockHistoricalPriceChange(todaysCloseStock, (todaysCloseStock.priceClose - stock.dbHistoricalPrice.priceClose)));
			}
		}
		
		Collections.sort(listOfChangesAgain, new ReflectiveComparator(). new ListComparator("changeInPriceClose"));
		for (DbStockHistoricalPriceChange changedStock : listOfChangesAgain){
			Co.println("Change B (+/-): " + changedStock.dbHistoricalPrice.symbol + " -> " + changedStock.changeInPriceClose);
		}
		
		ArrayList<ArrayList<String>> listOfRows = new ArrayList<ArrayList<String>>();		
		double lastPriceClose = 0;
	}
	
	public DbStockHistoricalPrice getDbStockHistoricalPrice(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice, String symbol){
		for (DbStockHistoricalPrice stock : listOfDbStockHistoricalPrice){
			if (stock.symbol.equals(symbol)){
				return stock;
			}
		}
		return null;
	}
	
	public class DbStockHistoricalPriceChange {
		public DbStockHistoricalPrice dbHistoricalPrice;
		public double changeInPriceClose;
		public DbStockHistoricalPriceChange(DbStockHistoricalPrice dbStockHistoricalPrice, double changeInPriceClose){
			this.dbHistoricalPrice = dbStockHistoricalPrice;
			this.changeInPriceClose = changeInPriceClose;
		}
	}
}
