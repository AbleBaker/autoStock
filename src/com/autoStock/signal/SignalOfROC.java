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
	private SignalMetricType signalMetricType = SignalMetricType.metric_roc;
	
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
		return new SignalMetric(signalMetricType.getSignalStrength(rocValue), signalMetricType);
	}
	
	public double getValue(){
		return rocValue;
	}
}
