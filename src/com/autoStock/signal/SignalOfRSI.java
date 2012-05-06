/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfRSI{
	private double rsiValue = 0;
	
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
		SignalMetric signalMetric = new SignalMetric(0, SignalTypeMetric.metric_rsi);
	
		SignalControl.setSignalStrengthForDI(signalMetric, rsiValue);
		
		return signalMetric;
	}
	
	public double getValue(){
		return rsiValue;
	}
}
