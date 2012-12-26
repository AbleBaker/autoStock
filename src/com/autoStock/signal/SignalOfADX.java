/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfADX{
	private double adxValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_adx;
	
	public SignalOfADX(double[] arrayOfDouble){
		adxValue = arrayOfDouble[arrayOfDouble.length-1];
	}
	
	public SignalMetric getSignal(){	
		return new SignalMetric(signalMetricType.getNormalizedValue(adxValue), signalMetricType);
	}
	
	public double getValue(){
		return adxValue;
	}
}
