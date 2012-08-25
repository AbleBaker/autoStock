/**
 * 
 */
package com.autoStock.analysis;

import com.autoStock.analysis.results.ResultsDI;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfDI extends IndicatorBase {
	public ResultsDI results;
	
	public IndicatorOfDI(int periodLength, boolean preceedDataset, CommonAnlaysisData commonAnlaysisData) {
		super(periodLength, preceedDataset, commonAnlaysisData);
	}
	
	public ResultsDI analize(){
		results = new ResultsDI(endIndex+1);
		
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = commonAnlaysisData.arrayOfPriceClose;
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().plusDI(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength-1, new MInteger(), new MInteger(), results.arrayOfDIPlus);
		if (returnCode == RetCode.Success){
			returnCode = getTaLibCore().minusDI(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength-1, new MInteger(), new MInteger(), results.arrayOfDIMinus);
		}
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
