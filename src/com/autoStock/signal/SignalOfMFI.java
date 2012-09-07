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
	
	public SignalOfMFI(double[] arrayOfMFI, int periodAverage){
		if (arrayOfMFI.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfMFI.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfMFI.length-periodAverage; i<arrayOfMFI.length; i++){
				mfiValue += arrayOfMFI[i];
			}
			
			mfiValue /= periodAverage;
			
		}else{
			mfiValue = arrayOfMFI[arrayOfMFI.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalMetricType.metric_mfi);
		
		signalMetric.applyStength(mfiValue);
		
		return signalMetric;
	}
	
	public double getValue(){
		return mfiValue;
	}
}
