package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.tools.DataExtractor;
import com.autoStock.types.QuoteSlice;
import com.lowagie.text.pdf.ExtraEncoding;

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
		arrayOfPriceOpen = extractDoubleFromQuoteSlice(listOfQuoteSlice, "priceOpen");
		arrayOfPriceHigh = extractDoubleFromQuoteSlice(listOfQuoteSlice, "priceHigh");
		arrayOfPriceLow = extractDoubleFromQuoteSlice(listOfQuoteSlice, "priceLow");
		arrayOfPriceClose = extractDoubleFromQuoteSlice(listOfQuoteSlice, "priceClose");
		arrayOfDates = new DataExtractor().extractDate(listOfQuoteSlice, "dateTime").toArray(new Date[0]);
	}
	
	private static double[] extractDoubleFromQuoteSlice(ArrayList<QuoteSlice> listOfQuoteSlice, String field){
		double[] arrayOfDouble = new double[listOfQuoteSlice.size()];
		int i = 0;
		
		for (QuoteSlice quoteSlice : listOfQuoteSlice){
			if (field.equals("priceOpen")){arrayOfDouble[i] = quoteSlice.priceOpen;}
			if (field.equals("priceHigh")){arrayOfDouble[i] = quoteSlice.priceHigh;}
			if (field.equals("priceLow")){arrayOfDouble[i] = quoteSlice.priceLow;}
			if (field.equals("priceClose")){arrayOfDouble[i] = quoteSlice.priceClose;}
			
			i++;
		}

		return arrayOfDouble;
	}
}
