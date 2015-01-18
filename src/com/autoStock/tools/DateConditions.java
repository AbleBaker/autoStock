/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin
 *
 */
public class DateConditions {
	public static abstract class BaseDateCondition {
		public abstract boolean isValid(); 
	}
	
	public static class QuoteAvailableDateCondition extends BaseDateCondition {
		public Symbol symbol;
		public Exchange exchange;
		public Date date;
		
		public QuoteAvailableDateCondition(Symbol symbol, Exchange exchange, Date date) {
			this.symbol = symbol;
			this.exchange = exchange;
		}
		
		public QuoteAvailableDateCondition(HistoricalData historicalData) {
			this.exchange = historicalData.exchange;
			this.symbol = historicalData.symbol;
		}

		public void setDate(Date date){
			this.date = date;
		}

		@Override
		public boolean isValid() {
			HistoricalData historicalData = new HistoricalData(exchange, symbol, date, date, Resolution.min);
			historicalData.setStartAndEndDatesToExchange();
			ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.symbolName), new QueryArg(QueryArgs.exchange, historicalData.exchange.exchangeName), new QueryArg(QueryArgs.resolution, historicalData.resolution.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));
			
			return listOfResults.size() > 0;
		}
	}
}
