/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPoint;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalMetric {
	public int strength;
	public SignalMetricType signalMetricType;
	public SignalPoint signalPoint = SignalPoint.none;
	
	public SignalMetric(int strength, SignalMetricType signalTypeMetric) {
		this.strength = strength;
		this.signalMetricType = signalTypeMetric;
	}
	
	public SignalPoint getSignalPoint(boolean havePosition){
		if (!havePosition){
			if (strength >= signalMetricType.pointToSignalLongEntry){
				return SignalPoint.long_entry;
			}else if (strength <= signalMetricType.pointToSignalShortEntry){
				return SignalPoint.short_entry;
			}
		}else{
			if (strength <= signalMetricType.pointToSignalLongExit){
				return SignalPoint.long_exit;
			}else if (strength >= signalMetricType.pointToSignalShortExit){
				return SignalPoint.short_exit;
			}
		}
		
		return SignalPoint.none;
	}
	
	public SignalPoint getSignalPointExit(){		
		return SignalPoint.none;
	}
	
	public void applyStength(double input){
		strength = signalMetricType.getSignalStrength(input);
	}
}
