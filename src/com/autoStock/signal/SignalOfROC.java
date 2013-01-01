/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfROC extends SignalBase {
	private double rocValue = 0;
	
	public SignalOfROC(double[] arrayOfROC){
		super(SignalMetricType.metric_roc);
		if (arrayOfROC.length < 1){throw new IllegalArgumentException();}
		rocValue = arrayOfROC[arrayOfROC.length-1];
	}
	
	@Override
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(rocValue), signalMetricType);
	}
	
	public double getValue(){
		return rocValue;
	}
}
