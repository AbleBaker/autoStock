package com.autoStock.tables;

/**
 * @author Kevin Kowalewski
 *
 */
public class TableDefinitions {
	public static enum AsciiTables{
		equity_historical_price_live(new AsciiColumns[]{AsciiColumns.symbol, AsciiColumns.dateTime, AsciiColumns.price, AsciiColumns.derivedChange}), //, Columns.sizeVolume
		equity_historicla_price_db(new AsciiColumns[]{}),
		;
		
		AsciiColumns[] arrayOfColumns;
		
		AsciiTables (AsciiColumns[] arrayOfColumns){
			this.arrayOfColumns = arrayOfColumns;
		}
	}
	
	public static enum AsciiColumns {
		id,
		symbol,
		orderType,
		quantity,
		priceLimit,
		priceStop,
		goodAfterDate,
		goodUntilDate,
		priceAverageFill,
		exchange,
		currency,
		securityType,
		price,
		sizeBid,
		sizeAsk,
		sizeLast,
		sizeVolume,
		dateTime,
		derivedChange,
	}
}
