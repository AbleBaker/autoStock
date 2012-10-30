/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.Co;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfTRIX{
	private double trixValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_trix;
	
	public SignalOfTRIX(double[] arrayOfTRIX, int periodAverage){
		if (arrayOfTRIX.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfTRIX.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfTRIX.length-periodAverage; i<arrayOfTRIX.length; i++){
				trixValue += arrayOfTRIX[i];
			}
			
			trixValue /= periodAverage;
			
		}else{
			trixValue = arrayOfTRIX[arrayOfTRIX.length-1];
		}
	}
	
	public SignalMetric getSignal(){	
		return new SignalMetric(signalMetricType.getSignalStrength(trixValue), signalMetricType);
	}
	
	public double getValue(){
		return trixValue;
	}
}
