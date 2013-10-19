/**
 * 
 */
package com.autoStock.signal.signalMetrics;

import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfSTORSI extends SignalBase {
	
	public SignalOfSTORSI(SignalParameters signalParameters){
		super(SignalMetricType.metric_storsi, signalParameters);
	}
	
	public void addInput(double[] arrayOfPercentK, double[] arrayOfPercentD, int periodAverage){
		double percentKValue = 0;
		double percentDValue = 0;
		
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
		
		super.setInput(percentKValue);
	}
}
