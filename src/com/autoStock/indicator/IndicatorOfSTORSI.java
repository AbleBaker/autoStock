/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsSTORSI;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MAType;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfSTORSI extends IndicatorBase {
	public ResultsSTORSI results;
	
	public IndicatorOfSTORSI(MutableInteger periodLength,  int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsSTORSI analyize(){
		results = new ResultsSTORSI(50);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.stochRsi(0, endIndex, arrayOfPriceClose, 15, 12, 3, MAType.Trima, new MInteger(), new MInteger(), results.arrayOfPercentK, results.arrayOfPercentD);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
