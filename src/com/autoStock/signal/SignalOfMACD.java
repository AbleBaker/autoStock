/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfMACD{
	private double macdValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_macd;
	
	public SignalOfMACD(double[] arrayOfMACD){
		if (arrayOfMACD.length < 1){throw new IllegalArgumentException();}
		macdValue = arrayOfMACD[arrayOfMACD.length-1];
	}
	
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(macdValue), signalMetricType);
	}
	
	public double getValue(){
		return macdValue;
	}
}
