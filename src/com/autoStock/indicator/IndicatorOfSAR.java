/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsCCI;
import com.autoStock.indicator.results.ResultsSAR;
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
public class IndicatorOfSAR extends IndicatorBase {
	public ResultsSAR results;
	
	public IndicatorOfSAR(IndicatorParameters indicatorParameters, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(indicatorParameters, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsSAR analyize(){
		results = new ResultsSAR(indicatorParameters.periodLength.value -1); //SAR specific
		results.arrayOfDates = arrayOfDates;
		
		RetCode returnCode = taLibCore.sar(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, 0.02, 0.20, new MInteger(), new MInteger(), results.arrayOfSAR); 

		handleAnalysisResult(returnCode);
		
		return results;
	}
}
