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
	
	public IndicatorOfUO(ImmutableInteger periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsUO analyize(){
		results = new ResultsUO(endIndex+1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.ultOsc(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, 7, 14, 28, new MInteger(), new MInteger(), results.arrayOfUO);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
