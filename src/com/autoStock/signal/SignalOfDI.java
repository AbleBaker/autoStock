/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.Co;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfDI{
	private double diValue = 0;
	private SignalMetricType signalMetricType = SignalMetricType.metric_di;
	
	public SignalOfDI(double[] arrayOfDIPlus, double[] arrayOfDIMinus){

		double[] arrayOfDI = new double[arrayOfDIPlus.length];
		
		for (int i=0; i<arrayOfDI.length; i++){
			arrayOfDI[i] = arrayOfDIPlus[i] - arrayOfDIMinus[i];
		}
	
		diValue = arrayOfDI[arrayOfDI.length-1];
	}
	
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(diValue), signalMetricType);
	}
	
	public double getValue(){
		return diValue;
	}
}
