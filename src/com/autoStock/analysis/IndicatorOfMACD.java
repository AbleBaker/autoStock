package com.autoStock.analysis;

import com.autoStock.analysis.results.ResultsMACD;
import com.autoStock.taLib.MAType;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfMACD extends IndicatorBase {
	public ResultsMACD results;
	
	public IndicatorOfMACD(int periodLength, boolean preceedDataset, CommonAnlaysisData commonAnlaysisData) {
		super(periodLength, preceedDataset, commonAnlaysisData);
	}
	
	public ResultsMACD analize(){
		results = new ResultsMACD(endIndex+1);

		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = commonAnlaysisData.arrayOfPriceClose;
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().macdExt(0, endIndex, arrayOfPriceClose, periodLength/3, MAType.Kama, periodLength/2, MAType.Kama, periodLength/2, MAType.Ema, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
