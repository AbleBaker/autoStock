package com.autoStock.signal;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class SignalBaseWithPoint extends SignalBase {
	public SignalBaseWithPoint(SignalMetricType signalMetricType, SignalParameters signalParameters, AlgorithmBase algorithmBase) {
		super(signalMetricType, signalParameters, algorithmBase);
	}
	
	public abstract SignalPoint getSignalPoint(boolean havePosition, PositionType positionType);
	
//	@Override
//	public double getStrength() {
//		throw new IllegalAccessError("Don't call this");
//	}
}
