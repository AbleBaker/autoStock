/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfTRIX{
	private double trixValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_trix;
	
	public SignalOfTRIX(double[] arrayOfTRIX){
		trixValue = arrayOfTRIX[arrayOfTRIX.length-1];
	}
	
	public SignalMetric getSignal(){	
		return new SignalMetric(signalMetricType.getNormalizedValue(trixValue), signalMetricType);
	}
	
	public double getValue(){
		return trixValue;
	}
}
