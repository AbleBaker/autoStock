package com.autoStock.indicator;

import com.autoStock.Co;
import com.autoStock.adjust.IterableOfInteger;
import com.autoStock.indicator.results.ResultsMACD;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MAType;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfMACD extends IndicatorBase {
	public ResultsMACD results;
	public static ImmutableInteger immutableIntegerForShort = new ImmutableInteger(12);
	public static ImmutableInteger immutableIntegerForLong = new ImmutableInteger(26);
	public static ImmutableInteger immutableIntegerForEma = new ImmutableInteger(9);
	
	public IndicatorOfMACD(int periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsMACD analize(){
		results = new ResultsMACD(endIndex+1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode;
		
		if (periodLength <= 30){ //TODO: Fix this, periods are wrong
			returnCode = taLibCore.macd(0, endIndex, arrayOfPriceClose, 9, periodLength-8, 9, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		}else{
			returnCode = taLibCore.macd(0, endIndex, arrayOfPriceClose, immutableIntegerForShort.value, immutableIntegerForLong.value, immutableIntegerForEma.value, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		}
		
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
