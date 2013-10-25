/**
 * 
 */
package com.autoStock.indicator;

import java.util.Arrays;

import com.autoStock.indicator.results.ResultsTRIX;
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
public class IndicatorOfTRIX extends IndicatorBase {
	public ResultsTRIX results;
	
	public IndicatorOfTRIX(IndicatorParameters indicatorParameters, CommonAnalysisData commonAnlaysisData, Core taLibcore, SignalMetricType signalMetricType) {
		super(indicatorParameters, commonAnlaysisData, taLibcore, signalMetricType);
	}
	
	public ResultsTRIX analyize(){
		results = new ResultsTRIX(indicatorParameters.resultSetLength+1); //TRIX Specific
		
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = commonAnlaysisData.arrayOfPriceClose;
		
		RetCode returnCode = taLibCore.trix(0, endIndex, arrayOfPriceClose, indicatorParameters.periodLength.value/3, new MInteger(), new MInteger(), results.arrayOfTRIX);
		
		results.arrayOfTRIX = Arrays.copyOfRange(results.arrayOfTRIX, 1, indicatorParameters.resultSetLength +1); //TRIX Specific
		
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
