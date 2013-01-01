package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

public abstract class SignalBase {
	public SignalMetricType signalMetricType = SignalMetricType.none;
	public abstract SignalMetric getSignal();
	
	public SignalBase(SignalMetricType signalMetricType){
		this.signalMetricType = signalMetricType;
	}
}
