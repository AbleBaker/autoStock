/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsAR;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfAR extends IndicatorBase {
	public ResultsAR results;
	
	public IndicatorOfAR(ImmutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsAR analyize(){
		results = new ResultsAR(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.aroon(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, periodLength.value-1, new MInteger(), new MInteger(), results.arrayOfARUp, results.arrayOfARDown);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
