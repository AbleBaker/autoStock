/**
 * 
 */
package com.autoStock.exchange.results;

import java.util.ArrayList;

import com.autoStock.trading.platform.ib.definitions.MarketData.TickPriceFields;
import com.autoStock.trading.platform.ib.definitions.MarketData.TickSizeFields;
import com.autoStock.trading.platform.ib.definitions.MarketData.TickTypes;
import com.autoStock.trading.types.TypeMarketData;

/**
 * @author Kevin Kowalewski
 *
 */
public class ExResultMarketData {
	public class ExResultSetMarketData {
		public TypeMarketData typeMarketData;
		public ArrayList<ExResultRowMarketData> listOfExResultRowMarketData = new ArrayList<ExResultRowMarketData>();
		
		public ExResultSetMarketData(TypeMarketData typeMarketData){
			this.typeMarketData = typeMarketData;
		}
	}
	
	public static class ExResultRowMarketData{
		public TickTypes tickType;
		public TickPriceFields tickPriceField;
		public TickSizeFields tickSizeField;
		public String tickStringValue;
		public double value;
		
		public ExResultRowMarketData(TickPriceFields field, double value){
			this.tickType = TickTypes.type_price;
			this.tickPriceField = field;
			this.value = value;
		}
		
		public ExResultRowMarketData(TickSizeFields field, double value){
			this.tickType = TickTypes.type_size;
			this.tickSizeField = field;
			this.value = value;
		}
		
		public ExResultRowMarketData(String value){
			this.tickType = TickTypes.type_string;
			this.tickStringValue = value;
		}
	}	
}
