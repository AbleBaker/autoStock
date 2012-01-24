package com.autoStock.database;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseDefinitions {
	public static enum Tables {
		marketOrders(new Columns[]{Columns.id,
			Columns.symbol,
			Columns.orderType,
			Columns.quantity,
			Columns.priceLimit,
			Columns.priceStop,
			Columns.goodAfterDate,
			Columns.goodUntilDate,
			Columns.priceAverageFill}),
		;
		
		public Columns[] columns;
		
		Tables(Columns[] columns){
			this.columns = columns;
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
	
	public static enum Queries {
		
	}
}
