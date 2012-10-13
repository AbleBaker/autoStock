/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsRSI;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfRSI extends IndicatorBase{
	public ResultsRSI results;
	
	public IndicatorOfRSI(int periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsRSI analyize(){
		results = new ResultsRSI(endIndex+1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.rsi(0, endIndex, arrayOfPriceClose, periodLength-1, new MInteger(), new MInteger(), results.arrayOfRSI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
