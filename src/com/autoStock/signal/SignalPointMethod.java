package com.autoStock.signal;

import com.autoStock.Co;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPoint;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalPointMethod {
	public static enum SignalPointTactic {
		tatic_majority,
		tatic_change,
		tatic_combined,
		tatic_mixed
	}
	
	public static synchronized SignalPoint getSignalPoint(boolean havePosition, Signal signal, PositionType positionType, SignalPointTactic signalPointTactic){
		SignalPoint signalPoint;
		
		if (signalPointTactic == SignalPointTactic.tatic_majority){
			signalPoint = getSignalPointMajority(havePosition, positionType, signal);
		}else if (signalPointTactic == SignalPointTactic.tatic_change){
			signalPoint = getSignalPointChange(havePosition, positionType, signal);
		}else if (signalPointTactic == SignalPointTactic.tatic_combined){
			signalPoint = getSignalPointCombined(havePosition, positionType, signal);
		}else if (signalPointTactic == SignalPointTactic.tatic_mixed){
			throw new UnsupportedOperationException();
		}else{
			throw new UnsupportedOperationException();
		}
		
		return signalPoint;
	}
	
	private static SignalPoint getSignalPointCombined(boolean havePosition, PositionType positionType, Signal signal){
		SignalPoint signalPointCurrent = new SignalPoint();
		SignalPoint signalPointLast = new SignalPoint();
		
		for (SignalMetric signalMetric : signal.listOfSignalMetric){
			signalPointCurrent = signalMetric.getSignalPoint(havePosition, positionType);
			
			if (signalPointLast.signalPointType == SignalPointType.none && signalPointCurrent.signalPointType != SignalPointType.none){
				signalPointLast = signalPointCurrent;
				signalPointLast.signalMetricType = signalPointCurrent.signalMetricType;
			}else {
				if (signalPointLast != signalPointCurrent){
					return new SignalPoint();
				}
			}
		}
		
		Co.println("--> SignalPoint, type: " + signalPointLast.signalPointType.name() + ", " + signalPointLast.signalMetricType);
		
		return signalPointLast;
	}
	
	private static SignalPoint getSignalPointMajority(boolean havePosition, PositionType positionType, Signal signal){
		SignalPoint signalPoint = new SignalPoint();
		int occurenceCount = 0;
		boolean isEvenNumberOfMetrics = MathTools.isEven(signal.listOfSignalMetric.size());

		for (SignalMetric signalMetric : signal.listOfSignalMetric){signalMetric.getSignalPoint(havePosition, positionType).occurences = 0;}
		for (SignalMetric signalMetric : signal.listOfSignalMetric){signalMetric.getSignalPoint(havePosition, positionType).occurences++;}
		
		for (SignalMetric signalMetric : signal.listOfSignalMetric){
			SignalPoint signalPointLocal = signalMetric.getSignalPoint(havePosition, positionType);
			if (signalPointLocal.occurences > occurenceCount || (isEvenNumberOfMetrics && signalPointLocal.signalPointType != SignalPointType.none)){
				signalPoint = signalPointLocal;
				occurenceCount = signalPointLocal.occurences;
			}
		}
//		Co.println("--> Majority is: " + signalPoint.name() + ", " + occurenceCount);
		return signalPoint;
	} 
	
	private static SignalPoint getSignalPointChange(boolean havePosition, PositionType positionType, Signal signal){
		SignalPoint signalPoint = new SignalPoint();
		
		for (SignalMetric signalMetric : signal.listOfSignalMetric){
			SignalPoint metricSignalPoint = signalMetric.getSignalPoint(havePosition, positionType);
			if (metricSignalPoint.signalPointType != SignalPointType.none){
				signalPoint = metricSignalPoint;
				signalPoint.signalMetricType = signalMetric.signalMetricType;
				break;
			}
		}
		
//		Co.println("--> Change is: " + signalPoint.name() + ", " + signalPoint.signalMetricType.name());
		
		return signalPoint;
	}
}
