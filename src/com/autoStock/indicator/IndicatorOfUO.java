/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsUO;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.MathTools;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfUO extends IndicatorBase {
	public ResultsUO results;
	private boolean average = false;
	
	public IndicatorOfUO(MutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsUO analyize(){
		results = new ResultsUO(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
//		for (int i=0;i<commonAnlaysisData.arrayOfPriceClose.length; i++){
//			Co.print(" " + commonAnlaysisData.arrayOfPriceClose[i]);
//		}
//		
//		Co.println("");
		
		RetCode returnCode;
		
		if (average){
			returnCode = taLibCore.ultOsc(0, endIndex, MathTools.averageArray(arrayOfPriceHigh), MathTools.averageArray(arrayOfPriceLow), MathTools.averageArray(arrayOfPriceClose), 7, 14, endIndex, new MInteger(), new MInteger(), results.arrayOfUO);
		}else{
			returnCode = taLibCore.ultOsc(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, 7, 14, endIndex-resultsetLength+1, new MInteger(), new MInteger(), results.arrayOfUO);
		}
		
		
		Co.println("--> Result set length: " + results.arrayOfUO.length);
		
		for(int i=0;i<results.arrayOfUO.length;i++){
			Co.print(" " + results.arrayOfUO[i]);
		}
		
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
