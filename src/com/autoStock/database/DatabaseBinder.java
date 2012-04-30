/**
 * 
 */
package com.autoStock.database;

import com.autoStock.database.queryResults.QueryResult;
import com.autoStock.generated.basicDefinitions.TableDefinitions;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbExchange;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbSymbol;
import com.autoStock.tools.DateTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseBinder {
	public DbStockHistoricalPrice getDbStockHistoricalPrice(long id, String symbol, double priceOpen, double priceHigh, double priceLow, double priceClose, int sizeVolume, String date){
		DbStockHistoricalPrice dbStockHistoricalPrice = new TableDefinitions.DbStockHistoricalPrice();
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
		DbSymbol dbSymbol = new TableDefinitions.DbSymbol();
		dbSymbol.id = id;
		dbSymbol.symbol = symbol;
		dbSymbol.exchange = exchange;
		dbSymbol.description = description;
		return dbSymbol;
	}
	
	public DbExchange getDbExchange(long id, String exchange, String currency, String timeOpen, String timeClose, String timeOffset){
		DbExchange dbExchange = new TableDefinitions.DbExchange();
		dbExchange.id = id;
		dbExchange.exchange = exchange;
		dbExchange.currency = currency;
		dbExchange.timeOpen = DateTools.getTimeFromString(timeOpen);
		dbExchange.timeClose = DateTools.getTimeFromString(timeClose);
		dbExchange.timeOffset = DateTools.getTimeFromString(timeOffset);
		return dbExchange;
	}

	public Object getQrSymbolCountFromExchange(String symbol, int count, long sizeVolume) {
		return new QueryResult.QrSymbolCountFromExchange(symbol, count, sizeVolume);
	}
}
