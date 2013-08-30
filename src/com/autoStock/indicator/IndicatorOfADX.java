/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsADX;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfADX extends IndicatorBase {
	public ResultsADX results;
	
	public IndicatorOfADX(MutableInteger periodLength, int resultLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsADX analyize(){
		results = new ResultsADX(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.adx(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength.value/2, new MInteger(), new MInteger(), results.arrayOfADX);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
