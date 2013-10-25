/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsAR;
import com.autoStock.signal.SignalDefinitions.IndicatorParameters;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfAR extends IndicatorBase {
	public ResultsAR results;
	
	public IndicatorOfAR(IndicatorParameters indicatorParameters, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(indicatorParameters, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsAR analyize(){
		results = new ResultsAR(indicatorParameters.resultSetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.aroon(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, indicatorParameters.periodLength.value-1, new MInteger(), new MInteger(), results.arrayOfARUp, results.arrayOfARDown);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
