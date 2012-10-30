/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfMFI{
	private double mfiValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_mfi;
	
	public SignalOfMFI(double[] arrayOfMFI){
		if (arrayOfMFI.length < 1){throw new IllegalArgumentException();}
		
		mfiValue = arrayOfMFI[arrayOfMFI.length-1];
	}
	
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(mfiValue), signalMetricType);
	}
	
	public double getValue(){
		return mfiValue;
	}
}
