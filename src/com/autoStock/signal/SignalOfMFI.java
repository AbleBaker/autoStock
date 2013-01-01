/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfMFI extends SignalBase {
	private double mfiValue = 0;
	
	public SignalOfMFI(double[] arrayOfMFI){
		super(SignalMetricType.metric_mfi);
		
		if (arrayOfMFI.length < 1){throw new IllegalArgumentException();}
		mfiValue = arrayOfMFI[arrayOfMFI.length-1];
	}

	@Override
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(mfiValue), signalMetricType);
	}
	
	public double getValue(){
		return mfiValue;
	}
}
