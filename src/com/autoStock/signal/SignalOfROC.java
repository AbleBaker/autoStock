/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfROC{
	private double rocValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_roc;
	
	public SignalOfROC(double[] arrayOfROC){
		if (arrayOfROC.length < 1){throw new IllegalArgumentException();}
		rocValue = arrayOfROC[arrayOfROC.length-1];
	}
	
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(rocValue), signalMetricType);
	}
	
	public double getValue(){
		return rocValue;
	}
}
