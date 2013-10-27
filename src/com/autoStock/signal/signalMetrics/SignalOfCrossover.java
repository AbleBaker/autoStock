package com.autoStock.signal.signalMetrics;

import java.awt.IllegalComponentStateException;

import org.apache.commons.lang3.mutable.MutableInt;

import com.autoStock.Co;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.SignalBaseWithPoint;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCrossover extends SignalBaseWithPoint {
	public MutableInteger longGapSize = new MutableInteger(5);
	public MutableInteger shortGapSize = new MutableInteger(-10);

	public SignalOfCrossover(SignalMetricType signalMetricType, SignalParameters signalParameters) {
		super(signalMetricType, signalParameters);
	}
	
	public void setInput(double valueFirst, double valueSecond){
		Co.println("--> ? " + valueFirst + ", " + valueSecond);
		super.setInput((valueSecond - valueFirst) * 100);
	}
	
	@Override
	public void setInput(double value) {
		throw new IllegalAccessError("Use the other signature");
	}

	@Override
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
		double currentGap = getStrength();
		
		Co.println("--> Current gap: " + currentGap);
		
		if (currentGap > longGapSize.value && havePosition == false){
			return new SignalPoint(SignalPointType.long_entry, signalMetricType); 
		}else if (currentGap <= 0 && havePosition){
			if (positionType == PositionType.position_long){
				return new SignalPoint(SignalPointType.long_exit, signalMetricType);
			}else if (positionType == PositionType.position_short){
				return new SignalPoint(SignalPointType.short_exit, signalMetricType);	
			}
		}else if (currentGap < shortGapSize.value && havePosition == false){
//			return new SignalPoint(SignalPointType.short_entry, signalMetricType);
		}
		
		return new SignalPoint();
	}
}
