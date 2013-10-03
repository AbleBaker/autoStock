package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsPTD;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfPTD extends IndicatorBase {
	private ResultsPTD results;

	public IndicatorOfPTD(MutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsPTD analyize(int[] arrayOfValues){
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		results = new ResultsPTD(periodLength.value);
		
		for (int i=0; i<arrayOfValues.length; i++){
			
		}
		
		return results;
	}
}
