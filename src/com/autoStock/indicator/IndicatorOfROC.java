/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsROC;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfROC extends IndicatorBase{
	public ResultsROC results;
	
	public IndicatorOfROC(int periodLength, CommonAnlaysisData commonAnlaysisData) {
		super(periodLength, commonAnlaysisData);
	}
	
	public ResultsROC analyize(){
		results = new ResultsROC(endIndex+1);
		
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = commonAnlaysisData.arrayOfPriceClose;
		
		RetCode returnCode = getTaLibCore().roc(0, endIndex, arrayOfPriceClose, periodLength-1, new MInteger(), new MInteger(), results.arrayOfROC);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
