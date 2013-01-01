/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsCCI;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfCCI extends IndicatorBase {
	public ResultsCCI results;
	
	public IndicatorOfCCI(int periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsCCI analyize(){
		results = new ResultsCCI(endIndex+1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		arrayOfPriceHigh = MathTools.averageArray(arrayOfPriceHigh);
		arrayOfPriceLow = MathTools.averageArray(arrayOfPriceLow);
		arrayOfPriceClose = MathTools.averageArray(arrayOfPriceClose);
		
		RetCode returnCode = taLibCore.cci(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength, new MInteger(), new MInteger(), results.arrayOfCCI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
