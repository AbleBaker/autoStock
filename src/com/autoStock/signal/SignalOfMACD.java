/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfMACD extends SignalBase {
	private double macdValue = 0;

	public SignalOfMACD(double[] arrayOfMACD){
		super(SignalMetricType.metric_macd);
		if (arrayOfMACD.length < 1){throw new IllegalArgumentException();}
		macdValue = arrayOfMACD[arrayOfMACD.length-1];
	}
	
	@Override
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(macdValue), signalMetricType);
	}
	
	public double getValue(){
		return macdValue;
	}
}
