package com.autoStock.signal;

import com.autoStock.Co;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalPoint;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalPointMethod {
	public static enum SignalPointTactic {
		tatic_majority,
		tatic_change,
		tatic_combined_test,
		tatic_entire
	}
	
	public static synchronized SignalPoint getSignalPoint(boolean havePosition, Signal signal, PositionType positionType, SignalPointTactic signalPointTactic){
		SignalPoint signalPoint = SignalPoint.none;
		
		if (signalPointTactic == SignalPointTactic.tatic_majority){
			signalPoint = getSignalPointMajority(havePosition, positionType, signal);
		}else if (signalPointTactic == SignalPointTactic.tatic_change){
			signalPoint = getSignalPointChange(havePosition, positionType, signal);
		}else if (signalPointTactic == SignalPointTactic.tatic_entire){
			throw new UnsupportedOperationException();
		}else{
			throw new UnsupportedOperationException();
		}
		
		return signalPoint;
	}
	
	private static SignalPoint getSignalPointMajority(boolean havePosition, PositionType positionType, Signal signal){
		SignalPoint signalPoint = SignalPoint.none;

		for (SignalMetric signalMetric : signal.listOfSignalMetric){signalMetric.getSignalPoint(havePosition, positionType).occurences++;}
		
		for (SignalMetric signalMetric : signal.listOfSignalMetric){
			if (signalMetric.getSignalPoint(havePosition, positionType) != SignalPoint.none){ //signalMetric.getSignalPoint(havePosition, positionType).occurences > occurences &&
				signalPoint = signalMetric.getSignalPoint(havePosition, positionType);
//				Co.println("--> Have signal at: " + signalMetric.signalMetricType.name() + ", " + signalMetric.strength + ", " + signalPoint.name());
			}
		}
	
		return signalPoint;
	} 
	
	private static SignalPoint getSignalPointChange(boolean havePosition, PositionType positionType, Signal signal){
		SignalPoint signalPoint = SignalPoint.none;
		
		for (SignalMetric signalMetric : signal.listOfSignalMetric){
			SignalPoint metricSignalPoint = signalMetric.getSignalPoint(havePosition, positionType);
			if (metricSignalPoint != SignalPoint.none){
//				Co.println("--> SignalPointChange metric, current signal, metric signal point: " + signalMetric.signalMetricType.name() + ", " + signalMetric.strength + ", " + metricSignalPoint.name());
				signalPoint = metricSignalPoint;
				signalPoint.signalMetricType = signalMetric.signalMetricType;
				break;
			}
		}
		
		return signalPoint;
	}
}
