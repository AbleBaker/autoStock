package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class CommonAnlaysisData {
	public boolean isInitialized = false;
	public double[] arrayOfPriceOpen;
	public double[] arrayOfPriceHigh;
	public double[] arrayOfPriceLow;
	public double[] arrayOfPriceClose;
	public Date[] arrayOfDates;
	
	public void setAnalysisData(ArrayList<QuoteSlice> listOfQuoteSlice){		
		arrayOfPriceOpen = extractDoubleFromQuoteSlice(listOfQuoteSlice, "priceOpen");
		arrayOfPriceHigh = extractDoubleFromQuoteSlice(listOfQuoteSlice, "priceHigh");
		arrayOfPriceLow = extractDoubleFromQuoteSlice(listOfQuoteSlice, "priceLow");
		arrayOfPriceClose = extractDoubleFromQuoteSlice(listOfQuoteSlice, "priceClose");
		arrayOfDates = extractDateFromQuoteSlice(listOfQuoteSlice, "dateTime");
		
		isInitialized = true;
	}
	
	@Deprecated
	public void appendAnalysisData(QuoteSlice quoteSlice){
		double[] arrayOfPriceOpenAppend = new double[arrayOfPriceOpen.length+1];
		double[] arrayOfPriceHighAppend = new double[arrayOfPriceHigh.length+1];
		double[] arrayOfPriceLowAppend = new double[arrayOfPriceLow.length+1];
		double[] arrayOfPriceCloseAppend = new double[arrayOfPriceClose.length+1];
		Date[] arrayOfDatesAppend= new Date[arrayOfDates.length+1];
		
		System.arraycopy(arrayOfPriceOpen, 0, arrayOfPriceOpenAppend, 0, arrayOfPriceOpen.length);
		System.arraycopy(arrayOfPriceHigh, 0, arrayOfPriceHighAppend, 0, arrayOfPriceHigh.length);
		System.arraycopy(arrayOfPriceLow, 0, arrayOfPriceLowAppend, 0, arrayOfPriceLow.length);
		System.arraycopy(arrayOfPriceClose, 0, arrayOfPriceCloseAppend, 0, arrayOfPriceClose.length);
		System.arraycopy(arrayOfDates, 0, arrayOfDatesAppend, 0, arrayOfDates.length);
		
		arrayOfPriceOpen = arrayOfPriceOpenAppend;
		arrayOfPriceHigh = arrayOfPriceHighAppend;
		arrayOfPriceLow = arrayOfPriceLowAppend;
		arrayOfPriceClose = arrayOfPriceCloseAppend;
		arrayOfDates = arrayOfDatesAppend;
		
		arrayOfPriceOpen[arrayOfPriceOpen.length-1] = quoteSlice.priceOpen;
		arrayOfPriceHigh[arrayOfPriceHigh.length-1] = quoteSlice.priceHigh;
		arrayOfPriceLow[arrayOfPriceLow.length-1] = quoteSlice.priceLow;
		arrayOfPriceClose[arrayOfPriceClose.length-1] = quoteSlice.priceClose;
		arrayOfDates[arrayOfDates.length-1] = quoteSlice.dateTime;
		
	}
	
	private Date[] extractDateFromQuoteSlice(ArrayList<QuoteSlice> listOfQuoteSlice, String field){
		Date[] arrayOfDate = new Date[listOfQuoteSlice.size()];
		
		int i = 0;
		
		for (QuoteSlice quoteSlice : listOfQuoteSlice){
			if (field.equals("dateTime")){
				arrayOfDate[i] = quoteSlice.dateTime;
			}
			
			i++;
		}
		
		return arrayOfDate;
	}
	
	private double[] extractDoubleFromQuoteSlice(ArrayList<QuoteSlice> listOfQuoteSlice, String field){
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
