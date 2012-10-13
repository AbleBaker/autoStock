package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsMACD;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MAType;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfMACD extends IndicatorBase {
	public ResultsMACD results;
	
	public IndicatorOfMACD(int periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsMACD analize(){
		results = new ResultsMACD(endIndex+1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.macdExt(0, endIndex, arrayOfPriceClose, periodLength/3, MAType.Ema, (int)(periodLength/2), MAType.Ema, periodLength/2, MAType.Ema, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
