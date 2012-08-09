/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;

import com.autoStock.exchange.results.ExResultMarketData.ExResultRowMarketData;
import com.autoStock.trading.platform.ib.definitions.MarketDataDefinitions.TickPriceFields;
import com.autoStock.trading.platform.ib.definitions.MarketDataDefinitions.TickSizeFields;
import com.autoStock.trading.platform.ib.definitions.MarketDataDefinitions.TickTypes;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class QuoteSliceTools {
	public QuoteSlice getQuoteSlice(ArrayList<ExResultRowMarketData> listOfExResultRowMarketData, Symbol symbol){
		
		QuoteSlice quoteSlice = new QuoteSlice();
		quoteSlice.symbol = symbol.symbol;
		
		for (ExResultRowMarketData resultRow : listOfExResultRowMarketData){		
			if (resultRow.tickType == TickTypes.type_price){
				if (resultRow.tickPriceField == TickPriceFields.field_open && quoteSlice.priceOpen == 0){
					quoteSlice.priceOpen = (float)resultRow.value;
				}
			
				else if (resultRow.tickPriceField == TickPriceFields.field_high){
					quoteSlice.priceHigh = (float)resultRow.value;
				}
				
				else if (resultRow.tickPriceField == TickPriceFields.field_low){
					quoteSlice.priceLow = (float)resultRow.value;
				}
				
				else if (resultRow.tickPriceField == TickPriceFields.field_close || resultRow.tickPriceField == TickPriceFields.field_last){
					quoteSlice.priceClose = (float)resultRow.value;
				}
				
				else if (resultRow.tickPriceField == TickPriceFields.field_bid){
					quoteSlice.priceBid = (float)resultRow.value;
				}
				
				else if (resultRow.tickPriceField == TickPriceFields.field_ask){
					quoteSlice.priceAsk = (float)resultRow.value;
				}
				
				else {
//					Co.println("No tickPriceField matched: " + resultRow.tickPriceField.name());
				}
			}
			
			else if (resultRow.tickType == TickTypes.type_size){
				if (resultRow.tickSizeField == TickSizeFields.field_volume){
					quoteSlice.sizeVolume = (int)resultRow.value;
				}
			}
			
			else if (resultRow.tickType == TickTypes.type_string){
				if (resultRow.tickStringValue != null){
					//Parse date
				}
			}
			
			else {
//				Co.println("No tickType matched: " + resultRow.tickType);
			}
		}
		
		return quoteSlice;
	}
}
