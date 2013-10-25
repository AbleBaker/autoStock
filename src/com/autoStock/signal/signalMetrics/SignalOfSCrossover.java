package com.autoStock.signal.signalMetrics;

import java.awt.IllegalComponentStateException;

import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfSCrossover extends SignalBase {

	public SignalOfSCrossover(SignalMetricType signalMetricType, SignalBase signalBaseFirst, SignalBase signalBaseSecond, SignalParameters signalParameters) {
		super(signalMetricType, signalParameters);
	}
	
	public void setInput(double valueFirst, double valueSecond){
		super.setInput(valueSecond - valueFirst);
	}
	
	@Override
	public void setInput(double value) {
		throw new IllegalComponentStateException("Not possible");
	}
}
