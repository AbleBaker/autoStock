/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsCCI;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfCCI extends IndicatorBase {
	public ResultsCCI results;
	
	public IndicatorOfCCI(int periodLength, CommonAnlaysisData commonAnlaysisData) {
		super(periodLength, commonAnlaysisData);
	}
	
	public ResultsCCI analyize(){
		results = new ResultsCCI(endIndex+1);
		
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		results.arrayOfPrice = commonAnlaysisData.arrayOfPriceClose;
		
		RetCode returnCode = getTaLibCore().cci(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength, new MInteger(), new MInteger(), results.arrayOfCCI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
