/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsCCI;
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
public class IndicatorOfCCI extends IndicatorBase {
	public ResultsCCI results;
	
	public IndicatorOfCCI(MutableInteger periodLength, int resultLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsCCI analyize(){
		results = new ResultsCCI(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.cci(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength.value, new MInteger(), new MInteger(), results.arrayOfCCI);
	
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
