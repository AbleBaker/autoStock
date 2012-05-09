/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsSTORSI;
import com.autoStock.taLib.MAType;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.DataExtractor;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisOfSTORSI extends AnalysisBase {
	public ResultsSTORSI results;
	
	public AnalysisOfSTORSI(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsSTORSI analyize(){
		results = new ResultsSTORSI(endIndex+1);
		
//		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<TypeQuoteSlice>)super.dataSource), "dateTime").toArray(new Date[0]);
//		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceClose").toArray(new Double[0]));
		
		//arrayOfPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceOpen").toArray(new Double[0]));
		//arrayOfPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceHigh").toArray(new Double[0]));
		//arrayOfPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceLow").toArray(new Double[0]));
		arrayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<QuoteSlice>)super.dataSource), "priceClose").toArray(new Double[0]));
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().stochRsi(0, endIndex, arrayOfPriceClose, periodLength-16, 10, 5, MAType.Dema, new MInteger(), new MInteger(), results.arrayOfPercentK, results.arrayOfPercentD);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
