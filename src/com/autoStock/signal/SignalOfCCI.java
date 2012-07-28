/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCCI{
	private double cciValue = 0;
	
	public SignalOfCCI(double[] arrayOfCCI, int periodAverage){
		if (arrayOfCCI.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfCCI.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfCCI.length-periodAverage; i<arrayOfCCI.length; i++){
				cciValue += arrayOfCCI[i];
			}
			
			cciValue /= periodAverage;
			
		}else{
			cciValue = arrayOfCCI[arrayOfCCI.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalMetricType.metric_cci);
	
		signalMetric.applyStength(cciValue);
		
		return signalMetric;
	}
	
	public double getValue(){
		return cciValue;
	}
}
