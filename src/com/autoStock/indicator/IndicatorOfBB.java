/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsBB;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MAType;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfBB extends IndicatorBase {
	public ResultsBB results;
	public int optionDeviationUp = 8;
	public int optionDeviationDown = 8;
	
	public IndicatorOfBB(ImmutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsBB analyize(){	
		results = new ResultsBB(resultsetLength);
		
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.bbands(0, endIndex, arrayOfPriceClose, periodLength.value, optionDeviationUp, optionDeviationDown, MAType.Kama, new MInteger(), new MInteger(), results.arrayOfUpperBand, results.arrayOfMiddleBand, results.arrayOfLowerBand);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
