package com.autoStock.signal;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class SignalWithSignalPoint extends SignalBase {
	public SignalWithSignalPoint(SignalMetricType signalMetricType, SignalParameters signalParameters) {
		super(signalMetricType, signalParameters);
	}
	
	public abstract SignalPoint getSignalPoint(boolean havePosition, PositionType positionType);
}
