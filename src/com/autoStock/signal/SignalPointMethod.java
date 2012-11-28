package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalPointType;

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
//			signalPoint = getSignalPointMajority(havePosition, positionType, signal);
			throw new UnsupportedOperationException();
		}else if (signalPointTactic == SignalPointTactic.tatic_change){
			signalPoint = getSignalPointChange(havePosition, positionType, signal);
		}else if (signalPointTactic == SignalPointTactic.tatic_combined){
//			signalPoint = getSignalPointCombined(havePosition, positionType, signal);
			throw new UnsupportedOperationException();
		}else if (signalPointTactic == SignalPointTactic.tatic_mixed){
			throw new UnsupportedOperationException();
		}else{
			throw new UnsupportedOperationException();
		}
		
		return signalPoint;
	}
	
//	private static SignalPoint getSignalPointCombined(boolean havePosition, PositionType positionType, Signal signal){
//		SignalPoint signalPoint = new SignalPoint();
//		
//		for (SignalMetric signalMetric : signal.listOfSignalMetric){
//			SignalPoint signalPointIterated = signalMetric.getSignalPoint(havePosition, positionType);
//				
//			if (signalPointIterated.signalPointType == SignalPointType.none){
//				signalPoint = new SignalPoint();
//				break;
//			}else if (signalPoint.signalPointType == SignalPointType.none){
//				signalPoint = signalPointIterated;
//			}else{
//				if (signalPointIterated.signalPointType != signalPoint.signalPointType){
//					signalPoint = new SignalPoint();
//					break;
//				}
//			}
//		}
//		
////		Co.println("--> SignalPoint, type: " + signalPoint.signalPointType.name() + ", " + signalPoint.signalMetricType);
//		
//		return signalPoint;
//	}
	
//	private static SignalPoint getSignalPointMajority(boolean havePosition, PositionType positionType, Signal signal){
//		SignalPoint signalPoint = new SignalPoint();
//		int occurenceCount = 0;
//		boolean isEvenNumberOfMetrics = MathTools.isEven(signal.listOfSignalMetric.size());
//		
//		ArrayList<SignalPointPair> listOfSignalPointPair = new ArrayList<SignalPointPair>();
//		
//		for (SignalMetric signalMetric : signal.listOfSignalMetric){
//			SignalPoint signalPointLocal = signalMetric.getSignalPoint(havePosition, positionType);
//			SignalPointPair signalPointPair = getPairForType(signalPointLocal.signalPointType, listOfSignalPointPair);
//			
//			if (signalPointPair == null){
//				listOfSignalPointPair.add(new SignalPointPair(signalPointLocal.signalPointType));
//			}else{
//				signalPointPair.occurences++;
//			}
//		}
//		
//		for (SignalPointPair signalPointPair : listOfSignalPointPair){
//			if (signalPointPair.occurences > occurenceCount || (isEvenNumberOfMetrics && signalPointPair.signalPointType != SignalPointType.none)){
//				signalPoint = new SignalPoint(signalPointPair.signalPointType, SignalMetricType.mixed);
//				occurenceCount = signalPointPair.occurences;
//			}
//		}
//		
////		Co.println("--> Majority is: " + signalPoint.signalPointType.name() + ", " + occurenceCount);
//		return signalPoint;
//	} 
	
	private static SignalPoint getSignalPointChange(boolean havePosition, PositionType positionType, Signal signal){
		SignalPoint signalPoint = new SignalPoint();
		
		for (SignalMetric signalMetric : signal.listOfSignalMetric){
			SignalPoint metricSignalPoint = signalMetric.getSignalPoint(havePosition, positionType, null, signal);
			if (metricSignalPoint.signalPointType != SignalPointType.none){
				signalPoint = metricSignalPoint;
				signalPoint.signalMetricType = signalMetric.signalMetricType;
				break;
			}
		}
		
//		Co.println("--> Change is: " + signalPoint.name() + ", " + signalPoint.signalMetricType.name());
		
		return signalPoint;
	}
	
	private static SignalPointPair getPairForType(SignalPointType signalPointType, ArrayList<SignalPointPair> listOfSignalPointPair){
		for (SignalPointPair signalPointPair : listOfSignalPointPair){
			if (signalPointPair.signalPointType == signalPointType){
				return signalPointPair;
			}
		}
		
		return null;
	}
	
	public static class SignalPointPair {
		public int occurences = 1;
		public SignalPointType signalPointType;
		
		public SignalPointPair(SignalPointType signalPointType){
			this.signalPointType = signalPointType;
		}
	}
}
