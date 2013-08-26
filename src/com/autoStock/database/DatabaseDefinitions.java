package com.autoStock.database;

import com.autoStock.database.queryResults.QueryResult;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbExchange;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbSymbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseDefinitions {
	
	public static class QueryArg{
		public QueryArgs queryArgs;
		public String value;
		
		public QueryArg(QueryArgs queryArgs, String value) {
			this.queryArgs = queryArgs;
			this.value = value;
		}
	}
	
	public enum QueryArgs{
		startDate,
		endDate,
		dayDate,
		symbol,
		exchange,
		limit,
	}
	
	
	public static enum BasicQueries {
		basic_historical_price_range("select * from stockHistoricalPrices where symbol = '%s' and dateTime between '%s' and '%s' order by dateTime asc", //GROUP BY ((60/10) * HOUR( dateTime ) + FLOOR( MINUTE( dateTime ) / 10 )) order by dateTime asc
			new QueryArgs[]{QueryArgs.symbol, QueryArgs.startDate, QueryArgs.endDate},
			DbStockHistoricalPrice.class,
			true
		),
		
		basic_single_date_sample_all_stocks("select * from stockHistoricalPrices where dateTime between '%s' and '%s' order by symbol asc",
				new QueryArgs[]{QueryArgs.startDate, QueryArgs.endDate},
				DbStockHistoricalPrice.class,
				true
			),
			
		basic_get_symbol_list_from_exchange("select * from symbols where exchange = '%s' order by rand() limit 100 ",
				new QueryArgs[]{QueryArgs.exchange},
				DbSymbol.class,
				true
			),
			
		basic_get_symbol_list_most_volume("select symbols.symbol, count(symbols.id), sum(sizeVolume) from stockHistoricalPrices left join symbols on symbols.symbol = stockHistoricalPrices.symbol where symbols.exchange = '%s' and (dateTime > '%s' and dateTime < '%s') group by symbols.symbol order by sum(sizeVolume) desc limit %s ",
				new QueryArgs[]{QueryArgs.startDate, QueryArgs.endDate, QueryArgs.exchange, QueryArgs.limit},
				QueryResult.QrSymbolCountFromExchange.class,
				true
			),
			
		basic_get_exchange_info("select * from exchanges where exchange = '%s'", 
				new QueryArgs[]{QueryArgs.exchange},
				DbExchange.class,
				true
			),
		;
		
		public final String query;
		public final QueryArgs[] listOfFormatterArguments;
		public final boolean isCachable; 
		public final Class resultClass;
		
		BasicQueries(String query, QueryArgs[] listOfFormatterArguments, Class resultClass, boolean cachable){
			this.query = query;
			this.listOfFormatterArguments = listOfFormatterArguments;
			this.resultClass = resultClass;
			this.isCachable = cachable;
		}
	}
}
