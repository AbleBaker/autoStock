/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.Co;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfADX{
	private double adxValue = 0;
	
	public SignalOfADX(double[] arrayOfADX, int periodAverage){
		if (arrayOfADX.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfADX.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfADX.length-periodAverage; i<arrayOfADX.length; i++){
				adxValue += arrayOfADX[i];
				Co.print("Added: " + arrayOfADX[i]);
			}
			
			adxValue /= periodAverage;
			
			Co.println(" average: " + adxValue);
			
		}else{
			adxValue = arrayOfADX[arrayOfADX.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalTypeMetric.metric_adx);
	
		SignalControl.setSignalStrengthForADX(signalMetric, adxValue);
		
		return signalMetric;
	}
	
	public double getValue(){
		return adxValue;
	}
}
