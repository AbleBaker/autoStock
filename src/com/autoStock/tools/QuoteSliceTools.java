/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;

import com.autoStock.exchange.results.ExResultMarketData.ExResultRowMarketData;
import com.autoStock.trading.platform.ib.definitions.MarketData.TickPriceFields;
import com.autoStock.trading.platform.ib.definitions.MarketData.TickSizeFields;
import com.autoStock.trading.platform.ib.definitions.MarketData.TickTypes;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class QuoteSliceTools {
	public TypeQuoteSlice getQuoteSlice(ArrayList<ExResultRowMarketData> listOfExResultRowMarketData){
		TypeQuoteSlice trRealtimeDataSlice = new TypeQuoteSlice();
		
		float priceHigh = 0;
		float priceLow = 0;
		
		for (ExResultRowMarketData resultRow : listOfExResultRowMarketData){
			if (resultRow.tickType == TickTypes.type_price){
				if (resultRow.tickPriceField == TickPriceFields.field_open && trRealtimeDataSlice.priceOpen == 0){
					trRealtimeDataSlice.priceOpen = (float)resultRow.value;
				}
			
				if (resultRow.tickPriceField == TickPriceFields.field_high){
					trRealtimeDataSlice.priceHigh = (float)resultRow.value;
				}
				
				if (resultRow.tickPriceField == TickPriceFields.field_low && trRealtimeDataSlice.priceLow > resultRow.value){
					trRealtimeDataSlice.priceLow = (float)resultRow.value;
				}
				
				if (resultRow.tickPriceField == TickPriceFields.field_close || resultRow.tickPriceField == TickPriceFields.field_last){
					trRealtimeDataSlice.priceClose = (float)resultRow.value;
				}
				
				if (resultRow.tickPriceField == TickPriceFields.field_bid){
					trRealtimeDataSlice.priceBid = (float)resultRow.value;
				}
				
				if (resultRow.tickPriceField == TickPriceFields.field_ask){
					trRealtimeDataSlice.priceAsk = (float)resultRow.value;
				}
			}
			
			if (resultRow.tickType == TickTypes.type_size){
				if (resultRow.tickSizeField == TickSizeFields.field_volume){
					trRealtimeDataSlice.sizeVolume = (int)resultRow.value;
				}
			}
		}
		
		return trRealtimeDataSlice;
	}
}
