/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfDI{
	private double diValue = 0;
	
	public SignalOfDI(double[] arrayOfDIPlus, double[] arrayOfDIMinus, int periodAverage){

		double[] arrayOfDI = new double[arrayOfDIPlus.length];
		
		for (int i=0; i<arrayOfDI.length; i++){
			arrayOfDI[i] = arrayOfDIPlus[i] - arrayOfDIMinus[i];
		}
		
		if (arrayOfDI.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfDI.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfDI.length-periodAverage; i<arrayOfDI.length; i++){
				diValue += arrayOfDI[i];
			}
			
			diValue /= periodAverage;
			
		}else{
			diValue = arrayOfDI[arrayOfDI.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalTypeMetric.metric_di);
		
		signalMetric.applyStength(diValue);
		
		return signalMetric;
	}
	
	public double getValue(){
		return diValue;
	}
}
