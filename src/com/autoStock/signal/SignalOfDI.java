/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfDI extends SignalBase {

	public SignalOfDI(SignalParameters signalParameters){
		super(SignalMetricType.metric_di, signalParameters);
	}
	
	@Override
	public void setInput(double value) {
		throw new UnsupportedOperationException();
	};
	
	public void setInput(double diPlus, double diMinus){
		super.setInput(diPlus - diMinus);
	}
}
