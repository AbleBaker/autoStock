package com.autoStock.database;

import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbSymbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseDefinitions {	
	
	public static enum QueryArgs{
		startDate,
		endDate,
		symbol,
		exchange,
		limit,
		;
		
		public String value;
		
		public QueryArgs setValue(String value){
			this.value = value;
			return this; // TODO: not thread safe 
		}
	}
	
	
	public static enum BasicQueries {
		basic_historical_price_range("select * from stockHistoricalPrices where symbol = '%s' and dateTime between '%s' and '%s' ", //GROUP BY ((60/10) * HOUR( dateTime ) + FLOOR( MINUTE( dateTime ) / 10 )) order by dateTime asc
			new QueryArgs[]{QueryArgs.symbol, QueryArgs.startDate, QueryArgs.endDate},
			DbStockHistoricalPrice.class
		),
		
		basic_single_date_sample_all_stocks("select * from stockHistoricalPrices where dateTime between '%s' and '%s' order by symbol asc",
				new QueryArgs[]{QueryArgs.startDate, QueryArgs.endDate},
				DbStockHistoricalPrice.class
			),
			
		basic_get_symbol_list_from_exchange("select * from symbols where exchange = '%s' order by rand() limit 100 ",
				new QueryArgs[]{QueryArgs.exchange},
				DbSymbol.class
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
