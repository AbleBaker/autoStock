/**
 * 
 */
package com.autoStock.analysis;

import com.autoStock.analysis.results.ResultsSTORSI;
import com.autoStock.taLib.MAType;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfSTORSI extends IndicatorBase {
	public ResultsSTORSI results;
	
	public IndicatorOfSTORSI(int periodLength, boolean preceedDataset, CommonAnlaysisData commonAnlaysisData) {
		super(periodLength, preceedDataset, commonAnlaysisData);
	}
	
	public ResultsSTORSI analyize(){
		results = new ResultsSTORSI(endIndex+1);
		
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().stochRsi(0, endIndex, arrayOfPriceClose, periodLength-16, 10, 5, MAType.Dema, new MInteger(), new MInteger(), results.arrayOfPercentK, results.arrayOfPercentD);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
