package com.autoStock.tables;

/**
 * @author Kevin Kowalewski
 *
 */
public class TableDefinitions {
	public static enum Tables{
		equity_historical_price_live(new Columns[]{Columns.symbol, Columns.price, Columns.sizeVolume}),
		equity_historicla_price_db(new Columns[]{}),
		;
		
		Columns[] arrayOfColumns;
		
		Tables (Columns[] arrayOfColumns){
			
		}
	}
	
	public static enum Columns {
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
	}
}
