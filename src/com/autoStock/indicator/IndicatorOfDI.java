/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsDI;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfDI extends IndicatorBase {
	public ResultsDI results;
	
	public IndicatorOfDI(ImmutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsDI analyize(){
		results = new ResultsDI(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.plusDI(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength.value-1, new MInteger(), new MInteger(), results.arrayOfDIPlus);
		if (returnCode == RetCode.Success){
			returnCode = taLibCore.minusDI(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength.value-1, new MInteger(), new MInteger(), results.arrayOfDIMinus);
		}
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
