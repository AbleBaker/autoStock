package com.autoStock.indicator.candleStick;

import com.autoStock.Co;
import com.autoStock.indicator.CommonAnlaysisData;
import com.autoStock.indicator.IndicatorBase;
import com.autoStock.indicator.candleStick.CandleStickDefinitions.CandleStickIdentity;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.sun.xml.internal.ws.handler.HandlerException;

/**
 * @author Kevin Kowalewski
 *
 */
public class CandleStickIdentifier extends IndicatorBase {
	private Core taLibCore;
	
	public CandleStickIdentifier(int periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
		this.taLibCore = taLibCore;
	}

	public CandleStickIdentifierResult identify(CandleStickIdentity candleStickIdentity){
		int [] arrayOfResults = new int[3];
		
		RetCode returnCode = taLibCore.cdlHangingMan(12, 14, commonAnlaysisData.arrayOfPriceOpen, commonAnlaysisData.arrayOfPriceHigh, commonAnlaysisData.arrayOfPriceLow, commonAnlaysisData.arrayOfPriceClose, new MInteger(), new MInteger(), arrayOfResults);
		handleAnalysisResult(returnCode);
		
		int i = 0;
		
		Co.println("\n");
		
		for (int integer : arrayOfResults){
			if (integer != 0){
				Co.println("--> RESULTS: " + arrayOfResults.length + ", " + i + " : " + integer);
//				throw new UnsupportedOperationException();
			}
			
			i++;
		}
		
		if (arrayOfResults[arrayOfResults.length-1] != 0){
//			throw new IllegalStateException();
		}
		
		return new CandleStickIdentifierResult(candleStickIdentity, commonAnlaysisData.arrayOfDates, arrayOfResults);
	}
}
