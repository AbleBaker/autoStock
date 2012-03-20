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
public class ExResultMarketOrder {
	public class ExResultSetMarketOrder {
		public TypePosition typePosition;
		public ArrayList<ExResultRowMarketOrder> listOfExResultRowMarketOrder = new ArrayList<ExResultRowMarketOrder>();
		
		public ExResultSetMarketOrder(TypePosition typePosition){
			this.typePosition = typePosition;
		}
	}
	
	public static class ExResultRowMarketOrder{
		double priceAvgFill;
		double priceLastFill;
		double comission;
		int filledUnits;
		int remainingUnits;
		int units;
		String status;
		
		public ExResultRowMarketOrder(double priceAvgFill, double priceLastFill, double comission, int filledUnits, int remainingUnits, int units, String status) {
			this.priceAvgFill = priceAvgFill;
			this.priceLastFill = priceLastFill;
			this.comission = comission;
			this.filledUnits = filledUnits;
			this.remainingUnits = remainingUnits;
			this.units = units;
			this.status = status;
		}
	}	
}
