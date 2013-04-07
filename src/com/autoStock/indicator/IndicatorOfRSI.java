/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsRSI;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfRSI extends IndicatorBase{
	public ResultsRSI results;
	
	public IndicatorOfRSI(ImmutableInteger periodLength, int resultsetLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsRSI analyize(){
		results = new ResultsRSI(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.rsi(0, endIndex, arrayOfPriceClose, periodLength.value-1, new MInteger(), new MInteger(), results.arrayOfRSI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
