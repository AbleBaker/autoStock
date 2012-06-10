/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsTRIX;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.DataExtractor;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisOfTRIX extends AnalysisBase {
	public ResultsTRIX results;
	
	public AnalysisOfTRIX(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsTRIX analyize(){
		results = new ResultsTRIX(endIndex+1);
		
		results.arrayOfDates = CommonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = CommonAnlaysisData.arrayOfPriceClose;
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().trix(0, endIndex, arrayOfPriceClose, periodLength/3, new MInteger(), new MInteger(), results.arrayOfTRIX);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
