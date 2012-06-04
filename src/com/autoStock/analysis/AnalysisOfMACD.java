package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsMACD;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.DataExtractor;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisOfMACD extends AnalysisBase {
	public ResultsMACD results;
	
	public AnalysisOfMACD(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsMACD analize(){
		results = new ResultsMACD(endIndex+1);
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<QuoteSlice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<QuoteSlice>)super.dataSource), "priceClose").toArray(new Double[0]));
		
		//arrayOfPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceOpen").toArray(new Double[0]));
		//arrayOfPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceHigh").toArray(new Double[0]));
		//arrayOfPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceLow").toArray(new Double[0]));
		arrayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<QuoteSlice>)super.dataSource), "priceClose").toArray(new Double[0]));
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		//int macdFastPeriod = AdjustmentCampaign.getInstance().getAdjustmentValueOfInt(AdjustmentDefinitions.analysis_macd_fast);
		//int macdSlowPeriod = periodLength / 2; //AdjustmentCampaign.getInstance().getAdjustmentValueOfInt(AdjustmentDefinitions.analysis_macd_slow);
		//int macdSignalPeriod = periodLength / 2; //AdjustmentCampaign.getInstance().getAdjustmentValueOfInt(AdjustmentDefinitions.analysis_macd_signal);
		
		//Co.println("macdFastPeirod: " + macdFastPeriod);
		
		RetCode returnCode = getTaLibCore().macd(0, endIndex, arrayOfPriceClose, periodLength/4, periodLength/2, periodLength/2, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
