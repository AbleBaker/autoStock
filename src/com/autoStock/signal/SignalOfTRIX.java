/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfTRIX extends SignalBase {
	private double trixValue = 0;
	
	public SignalOfTRIX(double[] arrayOfTRIX){
		super(SignalMetricType.metric_trix);
		trixValue = arrayOfTRIX[arrayOfTRIX.length-1];
	}
	
	@Override
	public SignalMetric getSignal(){	
		return new SignalMetric(signalMetricType.getNormalizedValue(trixValue), signalMetricType);
	}
	
	public double getValue(){
		return trixValue;
	}
}
