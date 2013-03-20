/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsCCI;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.MathTools;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfCCI extends IndicatorBase {
	public ResultsCCI results;
	
	public IndicatorOfCCI(ImmutableInteger periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsCCI analyize(){
		results = new ResultsCCI(endIndex+1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.cci(0, endIndex, MathTools.averageArray(arrayOfPriceHigh), MathTools.averageArray(arrayOfPriceLow), MathTools.averageArray(arrayOfPriceClose), periodLength.value, new MInteger(), new MInteger(), results.arrayOfCCI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
