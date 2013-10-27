/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsUO;
import com.autoStock.signal.SignalDefinitions.IndicatorParameters;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.MathTools;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfUO extends IndicatorBase {
	public ResultsUO results;
	
	public IndicatorOfUO(IndicatorParameters indicatorParameters, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(indicatorParameters, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsUO analyize(){
		results = new ResultsUO(indicatorParameters.resultSetLength);
		results.arrayOfDates = arrayOfDates;
		
		RetCode returnCode = taLibCore.ultOsc(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, 7, 14, endIndex-indicatorParameters.resultSetLength+1, new MInteger(), new MInteger(), results.arrayOfUO);
		
		handleAnalysisResult(returnCode);
		
		Co.println("--> Period length is: " + indicatorParameters.periodLength.value);
		
		return results;
	}
}
