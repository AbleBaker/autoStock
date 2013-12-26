package com.autoStock.signal.signalMetrics;

import com.autoStock.Co;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.ArrayTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfHT extends SignalBase {

	public SignalOfHT(SignalMetricType signalMetricType, SignalParameters signalParameters) {
		super(signalMetricType, signalParameters);
	}
	
	public double[] analyize(double[] arrayOfValues){
		double[] arrayOfResults = new double[28];
		
		if (arrayOfValues.length < 60){
			return arrayOfResults;
		}
		
		RetCode returnCode = new Core().htTrendline(0, 32, arrayOfValues, new MInteger(), new MInteger(), arrayOfResults);
		
//		for (int i=0; i<arrayOfValues.length; i++){
//			Co.println("--> Value: " + arrayOfValues[i]);
//		}
//		
//		Co.println("--> Return: " + returnCode.name() + ", " + arrayOfResults[0]);
		
		setInput(ArrayTools.getLastElement(arrayOfResults));
		
		return arrayOfResults;
	}

}
