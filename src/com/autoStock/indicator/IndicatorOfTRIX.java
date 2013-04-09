/**
 * 
 */
package com.autoStock.indicator;

import java.util.Arrays;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsTRIX;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfTRIX extends IndicatorBase {
	public ResultsTRIX results;
	
	public IndicatorOfTRIX(ImmutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibcore) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibcore);
	}
	
	public ResultsTRIX analyize(){
		results = new ResultsTRIX(resultsetLength+1); //TRIX Specific
		
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = commonAnlaysisData.arrayOfPriceClose;
		
		RetCode returnCode = taLibCore.trix(0, endIndex, arrayOfPriceClose, periodLength.value/3, new MInteger(), new MInteger(), results.arrayOfTRIX);
		
		results.arrayOfTRIX = Arrays.copyOfRange(results.arrayOfTRIX, 1, resultsetLength +1); //TRIX Specific
		
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
