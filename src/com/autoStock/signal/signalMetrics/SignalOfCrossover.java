package com.autoStock.signal.signalMetrics;

import java.awt.IllegalComponentStateException;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.SignalWithSignalPoint;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCrossover extends SignalWithSignalPoint {

	public SignalOfCrossover(SignalMetricType signalMetricType, SignalParameters signalParameters) {
		super(signalMetricType, signalParameters);
	}
	
	public void setInput(double valueFirst, double valueSecond){
		super.setInput(valueSecond - valueFirst);
	}
	
	@Override
	public void setInput(double value) {
		throw new IllegalComponentStateException("Not possible");
	}

	@Override
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
		if (havePosition == false){
			return new SignalPoint(SignalPointType.long_entry, signalMetricType);
		}else{
			return new SignalPoint(SignalPointType.long_exit, signalMetricType);
		}
	}
}
