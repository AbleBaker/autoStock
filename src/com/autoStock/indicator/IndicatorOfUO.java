/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsUO;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfUO extends IndicatorBase {
	public ResultsUO results;
	
	public IndicatorOfUO(ImmutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsUO analyize(){
		results = new ResultsUO(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.ultOsc(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, 7, 14, endIndex, new MInteger(), new MInteger(), results.arrayOfUO);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
