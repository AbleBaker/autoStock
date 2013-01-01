/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfWILLR extends SignalBase {
	private double willrValue = 0;
	
	public SignalOfWILLR(double[] arrayOfWILLR){
		super(SignalMetricType.metric_willr);
		if (arrayOfWILLR.length < 1){throw new IllegalArgumentException();}
		
		willrValue = arrayOfWILLR[arrayOfWILLR.length-1];
	}
	
	@Override
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(willrValue), signalMetricType);
	}
	
	public double getValue(){
		return willrValue;
	}
}
