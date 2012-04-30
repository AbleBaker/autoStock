/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsMACD;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.DataExtractor;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisMACD extends Analysis {
	public ResultsMACD results;
	
	public AnalysisMACD(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsMACD analize(){
		if (periodLength < 8){throw new UnsupportedOperationException();}
		results = new ResultsMACD(endIndex+1);
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<TypeQuoteSlice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceClose").toArray(new Double[0]));
		
		//arrayOfPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceOpen").toArray(new Double[0]));
		//arrayOfPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceHigh").toArray(new Double[0]));
		//arrayOfPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceLow").toArray(new Double[0]));
		arrayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceClose").toArray(new Double[0]));
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		//RetCode returnCode = getTaLibCore().macdExt(0, datasetLength+periodLength-1, valuesPriceClose, periodLength, MAType.Wma, periodLength, MAType.Wma, periodLength, MAType.Wma, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACD, results.arrayOfMACDHistogram);
		RetCode returnCode = getTaLibCore().macd(0, endIndex, arrayOfPriceClose, 4, 8, 4, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
