/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfRSI{
	private double rsiValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_rsi;
	
	public SignalOfRSI(double[] arrayOfRSI, int periodAverage){
		if (arrayOfRSI.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfRSI.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfRSI.length-periodAverage; i<arrayOfRSI.length; i++){
				rsiValue += arrayOfRSI[i];
			}
			
			rsiValue /= periodAverage;
			
		}else{
			rsiValue = arrayOfRSI[arrayOfRSI.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getSignalStrength(rsiValue), signalMetricType);
	}
	
	public double getValue(){
		return rsiValue;
	}
}
