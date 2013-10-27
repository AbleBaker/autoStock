package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalMoment {
	public final double strength;
	public final SignalMetricType signalMetricType;
	public final SignalPoint signalPoint;
	
	public SignalMoment(SignalMetricType signalMetricType, double strength, SignalPoint signalPoint) {
		this.strength = strength;
		this.signalMetricType = signalMetricType;
		this.signalPoint = signalPoint;
	}
}
