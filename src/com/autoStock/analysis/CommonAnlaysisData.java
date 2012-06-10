package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.tools.DataExtractor;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class CommonAnlaysisData {
	
	public static double[] arrayOfPriceOpen;
	public static double[] arrayOfPriceHigh;
	public static double[] arrayOfPriceLow;
	public static double[] arrayOfPriceClose;
	public static Date[] arrayOfDates;
	
	public static void setAnalysisData(ArrayList<QuoteSlice> listOfQuoteSlice){		
		arrayOfPriceOpen = ArrayUtils.toPrimitive(new DataExtractor().extractDouble(listOfQuoteSlice, "priceOpen").toArray(new Double[0]));
		arrayOfPriceHigh = ArrayUtils.toPrimitive(new DataExtractor().extractDouble(listOfQuoteSlice, "priceHigh").toArray(new Double[0]));
		arrayOfPriceLow = ArrayUtils.toPrimitive(new DataExtractor().extractDouble(listOfQuoteSlice, "priceLow").toArray(new Double[0]));
		arrayOfPriceClose = ArrayUtils.toPrimitive(new DataExtractor().extractDouble(listOfQuoteSlice, "priceClose").toArray(new Double[0]));
		arrayOfDates = new DataExtractor().extractDate(listOfQuoteSlice, "dateTime").toArray(new Date[0]);
	}
}
