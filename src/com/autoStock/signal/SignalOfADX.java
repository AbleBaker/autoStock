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
		if (arrayOfADX.length < 3){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfADX.length < periodAverage * 2){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfADX.length-periodAverage; i<arrayOfADX.length-1; i++){
				adxValue += arrayOfADX[i];
			}
			
			adxValue /= periodAverage;
			
		}else{
			adxValue = arrayOfADX[arrayOfADX.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalTypeMetric.metric_adx);
	
		SignalStrenghts.setSignalStrengthForADX(signalMetric, adxValue);
		
		return signalMetric;
	}
	
	public double getChange(){
		return adxValue;
	}
}
