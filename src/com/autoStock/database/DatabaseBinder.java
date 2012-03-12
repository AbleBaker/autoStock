/**
 * 
 */
package com.autoStock.database;

import java.util.Date;

import com.autoStock.generated.basicDefinitions.BasicTableDefinitions;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbSymbol;
import com.autoStock.tools.DateTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseBinder {
	public DbStockHistoricalPrice getDbStockHistoricalPrice(long id, String symbol, double priceOpen, double priceHigh, double priceLow, double priceClose, int sizeVolume, String date){
		DbStockHistoricalPrice dbStockHistoricalPrice = new BasicTableDefinitions(). new DbStockHistoricalPrice();
		dbStockHistoricalPrice.id = id;
		dbStockHistoricalPrice.symbol = symbol;
		dbStockHistoricalPrice.priceOpen = priceOpen;
		dbStockHistoricalPrice.priceHigh = priceHigh;
		dbStockHistoricalPrice.priceLow = priceLow;
		dbStockHistoricalPrice.priceClose = priceClose;
		dbStockHistoricalPrice.sizeVolume = sizeVolume;
		dbStockHistoricalPrice.dateTime = DateTools.getDateFromString(date);
		return dbStockHistoricalPrice;
	}
	
	public DbSymbol getDbSymbol(long id, String symbol, String exchange, String description){
		DbSymbol dbSymbol = new BasicTableDefinitions(). new DbSymbol();
		dbSymbol.id = id;
		dbSymbol.symbol = symbol;
		dbSymbol.exchange = exchange;
		dbSymbol.description = description;
		return dbSymbol;
	}
}
