/**
 * 
 */
package com.autoStock.tools;

import java.awt.List;
import java.util.ArrayList;
import java.util.Date;

import com.autoStock.Co;
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
			
		ArrayList<Double> listOfPriceLast = new ArrayList<Double>();
		ArrayList<Double> listOfPriceBid = new ArrayList<Double>();
		ArrayList<Double> listOfPriceAsk = new ArrayList<Double>();
		ArrayList<Integer> listOfSizeVolume = new ArrayList<Integer>();
		
		for (ExResultRowMarketData resultRow : listOfExResultRowMarketData){		
			if (resultRow.tickType == TickTypes.type_price){
				
				if (resultRow.tickPriceField == TickPriceFields.field_last){
					listOfPriceLast.add(resultRow.value);
				}
				
				else if (resultRow.tickPriceField == TickPriceFields.field_bid){
					listOfPriceBid.add(resultRow.value);
				}

				else if (resultRow.tickPriceField == TickPriceFields.field_ask){
					listOfPriceAsk.add(resultRow.value);
				}
				
				else {
//					Co.println("No tickPriceField matched: " + resultRow.tickPriceField.name());
				}
			}
			
			else if (resultRow.tickType == TickTypes.type_size){
				if (resultRow.tickSizeField == TickSizeFields.field_volume){
					listOfSizeVolume.add((int) resultRow.value);
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
		
		quoteSlice.priceOpen = getPriceOpen(listOfPriceLast);
		quoteSlice.priceHigh = getPriceHigh(listOfPriceLast);
		quoteSlice.priceLow = getPriceLow(listOfPriceLast);
		quoteSlice.priceClose = getPriceClose(listOfPriceLast);
		quoteSlice.priceBid = MathTools.getAverage(ArrayTools.getDoubleArray(listOfPriceBid));
		quoteSlice.priceAsk = MathTools.getAverage(ArrayTools.getDoubleArray(listOfPriceAsk));
		quoteSlice.sizeVolume = getSizeVolume(listOfSizeVolume);
		
		return quoteSlice;
	}
	
	public void mergeQuoteSlices(QuoteSlice quoteSlicePrevious, QuoteSlice quoteSlice) {
		if (quoteSlice.priceOpen == 0){quoteSlice.priceOpen = quoteSlicePrevious.priceOpen;}
		if (quoteSlice.priceHigh == 0){quoteSlice.priceHigh = quoteSlicePrevious.priceHigh;}
		if (quoteSlice.priceLow == 0) {quoteSlice.priceLow = quoteSlicePrevious.priceLow;}
		if (quoteSlice.priceClose == 0){quoteSlice.priceClose = quoteSlicePrevious.priceClose;}
		if (quoteSlice.priceBid == 0){quoteSlice.priceBid = quoteSlicePrevious.priceBid;}
		if (quoteSlice.priceAsk == 0){quoteSlice.priceAsk = quoteSlicePrevious.priceAsk;}
	}
	
	private double getPriceOpen(ArrayList<Double> listOfDouble){
		if (listOfDouble.size() > 0){return listOfDouble.get(0);}
		return 0;
	}
	
	private double getPriceHigh(ArrayList<Double> listOfDouble){
		double priceHigh = 0;
		for (double price : listOfDouble){
			if (price > priceHigh){
				priceHigh = price;
			}
		}
		
		return priceHigh;
	}
	
	private double getPriceLow(ArrayList<Double> listOfDouble){
		double priceLow = Double.MAX_VALUE;
		for (double price : listOfDouble){
			if (price < priceLow){
				priceLow = price;
			}
		}
		
		if (priceLow == Double.MAX_VALUE){
			return 0;
		}
		
		return priceLow;
	}
	
	private double getPriceClose(ArrayList<Double> listOfDouble){
		return listOfDouble.size() > 0 ? listOfDouble.get(listOfDouble.size()-1) : 0;
	}
	
	private int getSizeVolume(ArrayList<Integer> listOfInteger){
		if (listOfInteger.size() <= 2){return 0;}
		
		return listOfInteger.get(listOfInteger.size()-1) - listOfInteger.get(0);
	}
}