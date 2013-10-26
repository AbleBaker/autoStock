/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsEMA;
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
public class IndicatorOfEMA extends IndicatorBase {
	public ResultsEMA results;
	
	public IndicatorOfEMA(IndicatorParameters indicatorParameters, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(indicatorParameters, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsEMA analyize(){
		results = new ResultsEMA(indicatorParameters.resultSetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.ema(0, endIndex, commonAnlaysisData.arrayOfPriceClose, indicatorParameters.periodLength.value, new MInteger(), new MInteger(), results.arrayOfEMA);
		
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
