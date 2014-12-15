package com.autoStock.signal.signalMetrics;

import java.awt.IllegalComponentStateException;

import org.apache.commons.lang3.mutable.MutableInt;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsBase;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalDefinitions.SignalParametersForCrossover;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.SignalBaseWithPoint;
import com.autoStock.types.basic.MutableDouble;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCrossover extends SignalBaseWithPoint {
	public SignalOfCrossover(SignalMetricType signalMetricType, SignalParameters signalParameters) {
		super(signalMetricType, signalParameters);
	}
	
	@Override
	public void setInput(ResultsBase resultsBase) {
		setInput(0);
	}

	@Override
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
		double currentGap = getStrength();
		
//		Co.println("--> Current gap: " + currentGap);
		
		if (currentGap >= ((SignalParametersForCrossover)signalParameters).longGapSize.value && havePosition == false){
			return new SignalPoint(SignalPointType.long_entry, signalMetricType);
		}else if (currentGap <= 0 && havePosition && positionType == PositionType.position_long){
			return new SignalPoint(SignalPointType.long_exit, signalMetricType);
		}else if (currentGap >= 0 && havePosition && positionType == PositionType.position_short){
			return new SignalPoint(SignalPointType.short_exit, signalMetricType);
		}else if (currentGap <= ((SignalParametersForCrossover)signalParameters).shortGapSize.value && havePosition == false){
			return new SignalPoint(SignalPointType.short_entry, signalMetricType);
		}

		return new SignalPoint();
	}
}
