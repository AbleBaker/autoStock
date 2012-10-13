/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsTRIX;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfTRIX extends IndicatorBase {
	public ResultsTRIX results;
	
	public IndicatorOfTRIX(int periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibcore) {
		super(periodLength, commonAnlaysisData, taLibcore);
	}
	
	public ResultsTRIX analyize(){
		results = new ResultsTRIX(endIndex+1);
		
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = commonAnlaysisData.arrayOfPriceClose;
		
		RetCode returnCode = taLibCore.trix(0, endIndex, arrayOfPriceClose, periodLength/3, new MInteger(), new MInteger(), results.arrayOfTRIX);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
