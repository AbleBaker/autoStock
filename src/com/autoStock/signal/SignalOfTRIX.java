/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfTRIX{
	private double trixValue = 0;
	
	public SignalOfTRIX(double[] arrayOfTRIX, int periodAverage){
		if (arrayOfTRIX.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfTRIX.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfTRIX.length-periodAverage; i<arrayOfTRIX.length; i++){
				trixValue += arrayOfTRIX[i];
			}
			
			trixValue /= periodAverage;
			
		}else{
			trixValue = arrayOfTRIX[arrayOfTRIX.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalTypeMetric.metric_trix);
		
		signalMetric.applyStength(trixValue);
		
		return signalMetric;
	}
	
	public double getValue(){
		return trixValue;
	}
}
