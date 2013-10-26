/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsMFI;
import com.autoStock.signal.SignalDefinitions.IndicatorParameters;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.ArrayTools;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfMFI extends IndicatorBase{
	public ResultsMFI results;
	
	public IndicatorOfMFI(IndicatorParameters indicatorParameters, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(indicatorParameters, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsMFI analyize(){
		results = new ResultsMFI(indicatorParameters.resultSetLength);
		results.arrayOfDates = arrayOfDates;
		
		RetCode returnCode = taLibCore.mfi(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, ArrayTools.convertToDouble(arrayOfSizeVolume), indicatorParameters.periodLength.value-1, new MInteger(), new MInteger(), results.arrayOfMFI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
