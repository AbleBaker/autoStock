/**
 * 
 */
package com.autoStock.exchange.results;

import java.util.ArrayList;

import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class ExResultMarketOrder {
	public class ExResultSetMarketOrder {
		public Position typePosition;
		public ArrayList<ExResultRowMarketOrder> listOfExResultRowMarketOrder = new ArrayList<ExResultRowMarketOrder>();
		
		public ExResultSetMarketOrder(Position typePosition){
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
