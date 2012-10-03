/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPointType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalMetric {
	public int strength;
	public SignalMetricType signalMetricType;
	
	public SignalMetric(int strength, SignalMetricType signalTypeMetric) {
		this.strength = strength;
		this.signalMetricType = signalTypeMetric;
	}
	
	public synchronized SignalPoint getSignalPoint(boolean havePosition, PositionType positionType){
		if (havePosition == false){
			if (strength >= signalMetricType.pointToSignalLongEntry){
				return new SignalPoint(SignalPointType.long_entry, signalMetricType);
			}else if (strength <= signalMetricType.pointToSignalShortEntry){
				return new SignalPoint(SignalPointType.short_entry, signalMetricType);
			}
		}else{
			if (strength <= signalMetricType.pointToSignalLongExit && (positionType == PositionType.position_long_entry || positionType == PositionType.position_long)){
				return new SignalPoint(SignalPointType.long_exit, signalMetricType);
			}else if (strength >= signalMetricType.pointToSignalShortExit && (positionType == PositionType.position_short_entry || positionType == PositionType.position_short)){
				return new SignalPoint(SignalPointType.short_exit, signalMetricType);
			}
		}
		
		return new SignalPoint();
	}
}
