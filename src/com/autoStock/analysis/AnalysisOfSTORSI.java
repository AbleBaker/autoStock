/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsSTORSI;
import com.autoStock.taLib.MAType;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.DataExtractor;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisOfSTORSI extends AnalysisBase {
	public ResultsSTORSI results;
	
	public AnalysisOfSTORSI(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsSTORSI analyize(){
		results = new ResultsSTORSI(endIndex+1);
		
		results.arrayOfDates = CommonAnlaysisData.arrayOfDates;
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().stochRsi(0, endIndex, arrayOfPriceClose, periodLength-16, 10, 5, MAType.Dema, new MInteger(), new MInteger(), results.arrayOfPercentK, results.arrayOfPercentD);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
