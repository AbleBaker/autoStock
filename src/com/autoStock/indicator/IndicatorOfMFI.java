/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsMFI;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.ArrayTools;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfMFI extends IndicatorBase{
	public ResultsMFI results;
	
	public IndicatorOfMFI(ImmutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsMFI analyize(){
		results = new ResultsMFI(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.mfi(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, ArrayTools.convertToDouble(arrayOfSizeVolume), periodLength.value-1, new MInteger(), new MInteger(), results.arrayOfMFI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
