/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsADX;
import com.autoStock.analysis.results.ResultsRSI;
import com.autoStock.tools.DataExtractor;
import com.autoStock.types.TypeQuoteSlice;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisRSI extends Analysis{
	public ResultsRSI results;
	
	public AnalysisRSI(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsRSI analize(){
		results = new ResultsRSI(endIndex+1);
		
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<TypeQuoteSlice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		arrayOfPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceOpen").toArray(new Float[0]));
		arrayOfPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceHigh").toArray(new Float[0]));
		arrayOfPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceLow").toArray(new Float[0]));
		arrayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().rsi(0, endIndex, arrayOfPriceClose, periodLength, new MInteger(), new MInteger(), results.arrayOfRSI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
