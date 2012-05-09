/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsRSI;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.DataExtractor;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisOfRSI extends AnalysisBase{
	public ResultsRSI results;
	
	public AnalysisOfRSI(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsRSI analyize(){
		results = new ResultsRSI(endIndex+1);
		
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<QuoteSlice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<QuoteSlice>)super.dataSource), "priceClose").toArray(new Double[0]));
		
		arrayOfPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<QuoteSlice>)super.dataSource), "priceOpen").toArray(new Double[0]));
		arrayOfPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<QuoteSlice>)super.dataSource), "priceHigh").toArray(new Double[0]));
		arrayOfPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<QuoteSlice>)super.dataSource), "priceLow").toArray(new Double[0]));
		arrayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<QuoteSlice>)super.dataSource), "priceClose").toArray(new Double[0]));
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().rsi(0, endIndex, arrayOfPriceClose, periodLength, new MInteger(), new MInteger(), results.arrayOfRSI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
