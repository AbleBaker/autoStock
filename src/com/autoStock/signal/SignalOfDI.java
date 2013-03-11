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

	public SignalOfDI(){
		super(SignalMetricType.metric_di);
	}	
	
	public void addInput(double[] arrayOfDIPlus, double[] arrayOfDIMinus){
		double[] arrayOfDI = new double[arrayOfDIPlus.length];
		
		for (int i=0; i<arrayOfDI.length; i++){
			arrayOfDI[i] = arrayOfDIPlus[i] - arrayOfDIMinus[i];
		}
	
		super.addInput(arrayOfDI[arrayOfDI.length-1]);
	}
}
