/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsROC;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfROC extends IndicatorBase{
	public ResultsROC results;
	
	public IndicatorOfROC(ImmutableInteger periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsROC analyize(){
		results = new ResultsROC(endIndex+1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.roc(0, endIndex, arrayOfPriceClose, periodLength.value-1, new MInteger(), new MInteger(), results.arrayOfROC);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
