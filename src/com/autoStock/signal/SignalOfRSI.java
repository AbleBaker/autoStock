/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfRSI extends SignalBase {
	private double rsiValue = 0;
	
	public SignalOfRSI(double[] arrayOfRSI){
		super(SignalMetricType.metric_rsi);
		if (arrayOfRSI.length < 1){throw new IllegalArgumentException();}
		
		rsiValue = arrayOfRSI[arrayOfRSI.length-1];
	}
	
	@Override
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(rsiValue), signalMetricType);
	}
	
	public double getValue(){
		return rsiValue;
	}
}
