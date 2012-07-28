/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfSTORSI{
	private double percentKValue = 0;
	private double percentDValue = 0;
	
	public SignalOfSTORSI(double[] arrayOfPercentK, double[] arrayOfPercentD, int periodAverage){
		if (arrayOfPercentK.length < 1){throw new IllegalArgumentException();}
		if (arrayOfPercentD.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfPercentK.length < periodAverage){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfPercentD.length < periodAverage){throw new IllegalArgumentException();}

		if (periodAverage > 0){
			for (int i=arrayOfPercentK.length-periodAverage; i<arrayOfPercentK.length; i++){
				percentKValue += arrayOfPercentK[i];
			}
			
			for (int i=arrayOfPercentD.length-periodAverage; i<arrayOfPercentD.length; i++){
				percentDValue += arrayOfPercentD[i];
			}
			
			percentKValue /= periodAverage;
			percentDValue /= periodAverage;
			
		}else{
			percentKValue = arrayOfPercentK[arrayOfPercentK.length-1];
			percentDValue = arrayOfPercentK[arrayOfPercentD.length-1];
		}
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalMetricType.metric_storsi);
		signalMetric.applyStength(percentKValue);
		
		return signalMetric;
	}
	
	public double getValue(){
		return percentKValue;
	}
}
