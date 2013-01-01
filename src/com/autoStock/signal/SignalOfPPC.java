/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfPPC extends SignalBase {
	private double percentChange = 0;
	
	public SignalOfPPC(double[] arrayOfPrice, int periodAverage){
		super(SignalMetricType.metric_ppc);
		if (arrayOfPrice.length < 1){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfPrice.length < periodAverage * 2){throw new IllegalArgumentException();}
		
		double firstValue = 0;
		double secondValue = 0;
		
		if (periodAverage > 1){
			for (int i=0; i<periodAverage; i++){
				firstValue += arrayOfPrice[i];
			}
			
			for (int i=arrayOfPrice.length-periodAverage; i<arrayOfPrice.length; i++){
				secondValue += arrayOfPrice[i];
			}
			
			firstValue /= periodAverage;
			secondValue /= periodAverage;
		}else{
			firstValue = arrayOfPrice[arrayOfPrice.length-2];
			secondValue = arrayOfPrice[arrayOfPrice.length-1]; 
		}
		
		percentChange = secondValue / firstValue;
	}
	
	@Override
	public SignalMetric getSignal(){
		return new SignalMetric(signalMetricType.getNormalizedValue(percentChange), signalMetricType);
	}
	
	public double getValue(){
		return percentChange;
	}
}
