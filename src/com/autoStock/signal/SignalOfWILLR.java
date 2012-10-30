/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfWILLR{
	private double willrValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_willr;
	
	public SignalOfWILLR(double[] arrayOfWILLR){
		if (arrayOfWILLR.length < 1){throw new IllegalArgumentException();}
		
		willrValue = arrayOfWILLR[arrayOfWILLR.length-1];
	}
	
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(willrValue), signalMetricType);
	}
	
	public double getValue(){
		return willrValue;
	}
}
