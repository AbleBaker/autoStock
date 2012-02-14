package com.autoStock.database;

import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseDefinitions {	
	
	public static enum QueryArgs{
		startDate,
		endDate,
		symbol,
		;
		
		public String value;
		
		public QueryArgs setValue(String value){
			this.value = value;
			return this;
		}
	}
	
	
	public static enum BasicQueries {
		basic_historical_price_range("select * from stockHistoricalPrices where symbol = '%s' and dateTime between '%s' and '%s' GROUP BY ((60/10) * HOUR( dateTime ) + FLOOR( MINUTE( dateTime ) / 10 )) ",
			new QueryArgs[]{QueryArgs.symbol, QueryArgs.startDate, QueryArgs.endDate},
			DbStockHistoricalPrice.class
		),
		;
		
		public String query;
		public QueryArgs[] listOfFormatterArguments;
		public Class resultClass;
		
		BasicQueries(String query, QueryArgs[] listOfFormatterArguments, Class resultClass){
			this.query = query;
			this.listOfFormatterArguments = listOfFormatterArguments;
			this.resultClass = resultClass;
		}
	}
}
