/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsADX;
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
public class IndicatorOfSMA extends IndicatorBase {
	public ResultsADX results;
	
	public IndicatorOfSMA(IndicatorParameters indicatorParameters, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(indicatorParameters, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsADX analyize(){
		results = new ResultsADX(indicatorParameters.resultSetLength);
		results.arrayOfDates = arrayOfDates;
		
		RetCode returnCode = taLibCore.adx(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, indicatorParameters.periodLength.value/2, new MInteger(), new MInteger(), results.arrayOfADX);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
