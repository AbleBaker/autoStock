/**
 * 
 */
package com.autoStock.analysis;

import com.autoStock.analysis.results.ResultsRSI;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisOfRSI extends AnalysisBase{
	public ResultsRSI results;
	
	public AnalysisOfRSI(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsRSI analyize(){
		results = new ResultsRSI(endIndex+1);
		
		results.arrayOfDates = CommonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = CommonAnlaysisData.arrayOfPriceClose;
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().rsi(0, endIndex, arrayOfPriceClose, periodLength-1, new MInteger(), new MInteger(), results.arrayOfRSI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
