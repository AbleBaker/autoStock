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
public class SignalOfMACD{
	private double macdValue = 0;
	
	public SignalOfMACD(double[] arrayOfMACD, int periodAverage){
		if (arrayOfMACD.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfMACD.length < periodAverage){throw new IllegalArgumentException();}
		
		if (periodAverage > 0){
			for (int i=arrayOfMACD.length-periodAverage; i<arrayOfMACD.length; i++){
				macdValue += arrayOfMACD[i];
			}
			
			macdValue /= periodAverage;
			
		}else{
			macdValue = arrayOfMACD[arrayOfMACD.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalTypeMetric.metric_macd);
	
		SignalControl.setSignalStrengthForMACD(signalMetric, macdValue);
		
		return signalMetric;
	}
	
	public double getValue(){
		return macdValue;
	}
}