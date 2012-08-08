package com.autoStock.analysis;

import com.autoStock.analysis.results.ResultsMACD;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisOfMACD extends AnalysisBase {
	public ResultsMACD results;
	
	public AnalysisOfMACD(int periodLength, boolean preceedDataset, CommonAnlaysisData commonAnlaysisData) {
		super(periodLength, preceedDataset, commonAnlaysisData);
	}
	
	public ResultsMACD analize(){
		results = new ResultsMACD(endIndex+1);

		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = commonAnlaysisData.arrayOfPriceClose;
		
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
