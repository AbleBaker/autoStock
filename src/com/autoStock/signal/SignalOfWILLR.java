/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfWILLR{
	private double willrValue = 0;
	
	public SignalOfWILLR(double[] arrayOfWILLR, int periodAverage){
		if (arrayOfWILLR.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfWILLR.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfWILLR.length-periodAverage; i<arrayOfWILLR.length; i++){
				willrValue += arrayOfWILLR[i];
			}
			
			willrValue /= periodAverage;
			
		}else{
			willrValue = arrayOfWILLR[arrayOfWILLR.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalMetricType.metric_willr);
		
		signalMetric.applyStength(willrValue);
		
		return signalMetric;
	}
	
	public double getValue(){
		return willrValue;
	}
}
