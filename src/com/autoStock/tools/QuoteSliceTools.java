/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;

import com.autoStock.Co;
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
		TypeQuoteSlice typeQuoteSlice = new TypeQuoteSlice();
		
		float priceHigh = 0;
		float priceLow = 0;
		
		for (ExResultRowMarketData resultRow : listOfExResultRowMarketData){		
			if (resultRow.tickType == TickTypes.type_price){
				if (resultRow.tickPriceField == TickPriceFields.field_open && typeQuoteSlice.priceOpen == 0){
					typeQuoteSlice.priceOpen = (float)resultRow.value;
				}
			
				else if (resultRow.tickPriceField == TickPriceFields.field_high){
					typeQuoteSlice.priceHigh = (float)resultRow.value;
				}
				
				else if (resultRow.tickPriceField == TickPriceFields.field_low){ // && typeQuoteSlice.priceLow > resultRow.value
					typeQuoteSlice.priceLow = (float)resultRow.value;
				}
				
				else if (resultRow.tickPriceField == TickPriceFields.field_close || resultRow.tickPriceField == TickPriceFields.field_last){
					typeQuoteSlice.priceClose = (float)resultRow.value;
				}
				
				else if (resultRow.tickPriceField == TickPriceFields.field_bid){
					typeQuoteSlice.priceBid = (float)resultRow.value;
				}
				
				else if (resultRow.tickPriceField == TickPriceFields.field_ask){
					typeQuoteSlice.priceAsk = (float)resultRow.value;
				}
				
				else {
					Co.println("No tickPriceField matched: " + resultRow.tickPriceField.name());
				}
			}
			
			else if (resultRow.tickType == TickTypes.type_size){
				if (resultRow.tickSizeField == TickSizeFields.field_volume){
					typeQuoteSlice.sizeVolume = (int)resultRow.value;
				}
			}
			
			else {
				Co.println("No tickType mathced");
			}
		}
		
		return typeQuoteSlice;
	}
}
