/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfDI extends SignalBase {
	private double diValue = 0;
	
	public SignalOfDI(double[] arrayOfDIPlus, double[] arrayOfDIMinus){
		super(SignalMetricType.metric_di);
		double[] arrayOfDI = new double[arrayOfDIPlus.length];
		
		for (int i=0; i<arrayOfDI.length; i++){
			arrayOfDI[i] = arrayOfDIPlus[i] - arrayOfDIMinus[i];
		}
	
		diValue = arrayOfDI[arrayOfDI.length-1];
	}
	
	@Override
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(diValue), signalMetricType);
	}
	
	public double getValue(){
		return diValue;
	}
}
