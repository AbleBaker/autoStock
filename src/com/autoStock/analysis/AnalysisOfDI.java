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
public class AnalysisOfDI extends AnalysisBase {
	public ResultsDI results;
	
	public AnalysisOfDI(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsDI analize(){
		results = new ResultsDI(endIndex+1);
		
		results.arrayOfDates = CommonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = CommonAnlaysisData.arrayOfPriceClose;
		
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
