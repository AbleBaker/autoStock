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

		results.arrayOfDates = CommonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = CommonAnlaysisData.arrayOfPriceClose;
		
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
