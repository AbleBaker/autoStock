/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCCI{
	private double cciValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_cci;
	
	public SignalOfCCI(double[] arrayOfCCI){
		cciValue = arrayOfCCI[arrayOfCCI.length-1];
	}
	
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(cciValue), signalMetricType);
	}
	
	public double getValue(){
		return cciValue;
	}
}
