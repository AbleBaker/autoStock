/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfROC{
	private double rocValue = 0;
	
	public SignalOfROC(double[] arrayOfROC, int periodAverage){
		if (arrayOfROC.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfROC.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfROC.length-periodAverage; i<arrayOfROC.length; i++){
				rocValue += arrayOfROC[i];
			}
			
			rocValue /= periodAverage;
			
		}else{
			rocValue = arrayOfROC[arrayOfROC.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalMetricType.metric_roc);
		
		signalMetric.applyStength(rocValue);
		
		return signalMetric;
	}
	
	public double getValue(){
		return rocValue;
	}
}
