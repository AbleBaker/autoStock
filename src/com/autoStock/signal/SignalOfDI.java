/**
 * 
 */
package com.autoStock.signal;

import org.apache.http.MethodNotSupportedException;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfDI extends SignalBase {

	public SignalOfDI(){
		super(SignalMetricType.metric_di);
	}
	
	@Override
	public void setInput(double value) {
		
	};
	
	public void setInput(double diPlus, double diMinus){
		super.setInput(diPlus - diMinus);
	}
}
