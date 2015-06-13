/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;

/**
 * @author Kevin
 *
 */
public class SignalOfReentry extends SignalBaseWithPoint {

	public SignalOfReentry(SignalMetricType signalMetricType, SignalParameters signalParameters) {
		super(signalMetricType, signalParameters);
	}
	
	@Override
	public void setInputCached(double strength, double normalizedValue) {throw new IllegalAccessError("Don't call this");}
	
	@Override
	public void setInput(double value) {throw new IllegalAccessError("Don't call this");}

	@Override
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
		return null;
	}

}
