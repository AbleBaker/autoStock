/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfRSI{
	private double rsiValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_rsi;
	
	public SignalOfRSI(double[] arrayOfRSI){
		if (arrayOfRSI.length < 1){throw new IllegalArgumentException();}
		
		rsiValue = arrayOfRSI[arrayOfRSI.length-1];
	}
	
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(rsiValue), signalMetricType);
	}
	
	public double getValue(){
		return rsiValue;
	}
}
