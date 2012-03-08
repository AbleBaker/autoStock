/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsADX;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.DataConditioner;
import com.autoStock.tools.DataExtractor;
import com.autoStock.types.TypeQuoteSlice;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisCDL extends Analysis {
	public ResultsADX results;
	
	public AnalysisCDL(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsADX analize(){
		results = new ResultsADX(endIndex+1);
		
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<TypeQuoteSlice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		arrayOfPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceOpen").toArray(new Float[0]));
		arrayOfPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceHigh").toArray(new Float[0]));
		arrayOfPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceLow").toArray(new Float[0]));
		arrayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		
		RetCode returnCode;
//		returnCode = getTaLibCore().cdlAbandonedBaby(0, endIndex, arrayOfPriceOpen, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength, new MInteger(), new MInteger(), outInteger);
//		returnCode = getTaLibCore().cdlBreakaway(0, endIndex, arrayOfPriceOpen, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength, new MInteger(), new MInteger(), outInteger);
//		returnCode = getTaLibCore().
//		handleAnalysisResult(returnCode);
		
		return results;
	}
}
