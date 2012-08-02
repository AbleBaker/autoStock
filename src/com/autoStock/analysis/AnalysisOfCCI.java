/**
 * 
 */
package com.autoStock.analysis;

import com.autoStock.analysis.results.ResultsCCI;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisOfCCI extends AnalysisBase {
	public ResultsCCI results;
	
	public AnalysisOfCCI(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsCCI analyize(){
		results = new ResultsCCI(endIndex+1);
		
		results.arrayOfDates = CommonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = CommonAnlaysisData.arrayOfPriceClose;
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().cci(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength, new MInteger(), new MInteger(), results.arrayOfCCI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
