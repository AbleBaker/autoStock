/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsROC;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfROC extends IndicatorBase{
	public ResultsROC results;
	
	public IndicatorOfROC(MutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsROC analyize(){
		results = new ResultsROC(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.roc(0, endIndex, arrayOfPriceClose, periodLength.value-1, new MInteger(), new MInteger(), results.arrayOfROC);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
