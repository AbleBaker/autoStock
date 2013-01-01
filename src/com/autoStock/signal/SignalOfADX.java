/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfADX extends SignalBase {
	private double adxValue = 0;
	
	public SignalOfADX(double[] arrayOfDouble){
		super(SignalMetricType.metric_adx);
		adxValue = arrayOfDouble[arrayOfDouble.length-1];
		signalMetricType = SignalMetricType.metric_adx;
	}
	
	@Override
	public SignalMetric getSignal(){	
		return new SignalMetric(signalMetricType.getNormalizedValue(adxValue), signalMetricType);
	}
	
	public double getValue(){
		return adxValue;
	}
}
