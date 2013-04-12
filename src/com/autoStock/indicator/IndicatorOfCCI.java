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
	
	public IndicatorOfCCI(ImmutableInteger periodLength, int resultLength, CommonAnalysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, resultLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsCCI analyize(){
		results = new ResultsCCI(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode;
		
		boolean average = true;
		
		if (average){
			returnCode = taLibCore.cci(0, endIndex, MathTools.averageArray(arrayOfPriceHigh), MathTools.averageArray(arrayOfPriceLow), MathTools.averageArray(arrayOfPriceClose), periodLength.value, new MInteger(), new MInteger(), results.arrayOfCCI);
		}else{
			returnCode = taLibCore.cci(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength.value, new MInteger(), new MInteger(), results.arrayOfCCI);
		}
		
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
