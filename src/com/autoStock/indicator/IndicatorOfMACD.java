package com.autoStock.indicator;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsMACD;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfMACD extends IndicatorBase {
	public ResultsMACD results;
	public static MutableInteger immutableIntegerForLong = new MutableInteger(29);
	public static MutableInteger immutableIntegerForEma = new MutableInteger(9);
	public static MutableInteger immutableIntegerForShort = new MutableInteger(6);
	
	public IndicatorOfMACD(MutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsMACD analize(){
		results = new ResultsMACD(1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode;
		
		Co.println("--> Period length: " + periodLength.value + ", " + commonAnlaysisData.arrayOfDates.length);
		
//		if (periodLength.value <= 30){ //TODO: Fix this, periods are wrong
			returnCode = taLibCore.macd(0, endIndex, arrayOfPriceClose, 6, 22, 9, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		
//			returnCode = taLibCore.macdFix(0, 30, arrayOfPriceClose, 9, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
//		}else{
//			try {
//				returnCode = taLibCore.macd(0, endIndex, arrayOfPriceClose, immutableIntegerForShort.value, immutableIntegerForLong.value, 17, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
//				handleAnalysisResult(returnCode);
//			}catch(ArrayIndexOutOfBoundsException e){
//				e.printStackTrace();
//			}
//		}
		
		return results;
	}
}
