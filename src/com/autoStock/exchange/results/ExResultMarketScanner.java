/**
 * 
 */
package com.autoStock.exchange.results;

import java.util.ArrayList;

import com.autoStock.trading.platform.ib.core.TickType;
import com.autoStock.trading.platform.ib.definitions.MarketData.TickPriceFields;
import com.autoStock.trading.platform.ib.definitions.MarketData.TickSizeFields;
import com.autoStock.trading.platform.ib.definitions.MarketData.TickTypes;
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.trading.types.TypePosition;
import com.autoStock.trading.types.TypeRealtimeData;

/**
 * @author Kevin Kowalewski
 *
 */
public class ExResultMarketScanner {
	public class ExResultSetMarketScanner {
		public ArrayList<ExResultRowMarketScanner> listOfExResultRowMarketScanner = new ArrayList<ExResultRowMarketScanner>();
	}
	
	public static class ExResultRowMarketScanner{
		String symbol;
		int rank;
	}
}
