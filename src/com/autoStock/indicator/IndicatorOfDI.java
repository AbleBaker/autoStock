/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsDI;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfDI extends IndicatorBase {
	public ResultsDI results;
	
	public IndicatorOfDI(int periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsDI analize(){
		results = new ResultsDI(endIndex+1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.plusDI(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength-1, new MInteger(), new MInteger(), results.arrayOfDIPlus);
		if (returnCode == RetCode.Success){
			returnCode = taLibCore.minusDI(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength-1, new MInteger(), new MInteger(), results.arrayOfDIMinus);
		}
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
