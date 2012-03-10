/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.Co;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfPPC{
	private float percentChange = 0;
	
	public SignalOfPPC(float[] arrayOfPrice, int periodAverage){
		if (arrayOfPrice.length < 3){throw new IllegalArgumentException();}
		if (periodAverage > 0 && arrayOfPrice.length < periodAverage * 2){throw new IllegalArgumentException();}
		
		float firstValue = 0;
		float secondValue = 0;
		
		if (periodAverage > 0){
			for (int i=0; i<periodAverage; i++){
				firstValue += arrayOfPrice[i];
			}
			
			for (int i=arrayOfPrice.length-periodAverage; i<arrayOfPrice.length; i++){
				secondValue += arrayOfPrice[i];
			}
			
			firstValue /= periodAverage;
			secondValue /= periodAverage;
		}else{
			firstValue = arrayOfPrice[0];
			secondValue = arrayOfPrice[arrayOfPrice.length-1]; 
		}
		
		percentChange = secondValue / firstValue;
		//Co.println("First average, second: " + firstValue + "," + secondValue);
		//Co.println("PPC: " + arrayOfPriceClose.length + ","+ percentChange);
	}
	
	public SignalMetric getSignal(){
		SignalMetric signalMetric = new SignalMetric(0, SignalTypeMetric.metric_ppc);
		
		if (percentChange > 1.1){
			signalMetric.strength = 50;
		}else if (percentChange < 0.99){
			signalMetric.strength = -50;
		}else {
			//pass
		}
		
		return signalMetric;
	}
	
	public float getChange(){
		return percentChange;
	}
}
