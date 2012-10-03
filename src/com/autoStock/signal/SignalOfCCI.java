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
	private SignalMetricType signalMetricType = SignalMetricType.metric_cci;
	
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
		return new SignalMetric(signalMetricType.getSignalStrength(cciValue), signalMetricType);
	}
	
	public double getValue(){
		return cciValue;
	}
}
