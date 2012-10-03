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
	
	public SignalOfMACD(double[] arrayOfMACD, int periodAverage){
		if (arrayOfMACD.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfMACD.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfMACD.length-periodAverage; i<arrayOfMACD.length; i++){
				macdValue += arrayOfMACD[i];
			}
			
			macdValue /= periodAverage;
			
		}else{
			macdValue = arrayOfMACD[arrayOfMACD.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getSignalStrength(macdValue), signalMetricType);
	}
	
	public double getValue(){
		return macdValue;
	}
}
