package com.autoStock.indicator;

import com.autoStock.indicator.results.ResultsMACD;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfMACD extends IndicatorBase {
	public ResultsMACD results;
	public static ImmutableInteger immutableIntegerForLong = new ImmutableInteger(29);
	public static ImmutableInteger immutableIntegerForEma = new ImmutableInteger(9);
	public static ImmutableInteger immutableIntegerForShort = new ImmutableInteger(6);
	
	public IndicatorOfMACD(ImmutableInteger periodLength, int resultsetLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsMACD analize(){
		results = new ResultsMACD(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode;
		
		if (periodLength.value <= 30){ //TODO: Fix this, periods are wrong
			returnCode = taLibCore.macd(0, endIndex, arrayOfPriceClose, 9, periodLength.value-8, 9, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		}else{
			try {
				returnCode = taLibCore.macd(0, endIndex, arrayOfPriceClose, immutableIntegerForShort.value, immutableIntegerForLong.value, 17, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
				handleAnalysisResult(returnCode);
			}catch(ArrayIndexOutOfBoundsException e){
				e.printStackTrace();
			}
		}
		
		return results;
	}
}
