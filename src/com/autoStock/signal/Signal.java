/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPoint;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalTrend;

/**
 * @author Kevin Kowalewski
 *
 */
public class Signal {
	public SignalSource signalSource;
	public SignalTrend lastSignalType = SignalTrend.type_none;
	public SignalTrend currentSignalTrend = SignalTrend.type_none;
	public SignalPoint currentSignalPoint = SignalPoint.none;
	public ArrayList<SignalMetric> listOfSignalMetric = new ArrayList<SignalMetric>();
	
	public static enum SignalMajorityType { majority, whole, change}
	
	public Signal(SignalSource signalSource) {
		this.signalSource = signalSource;
	}
	
	public void addSignalMetrics(SignalMetric... signalMetrics){
		for (SignalMetric signalMetric : signalMetrics){
			listOfSignalMetric.add(signalMetric);
		}
	}
	
	public SignalMetric getSignalMetric(SignalMetricType signalTypeMetric ){
		for (SignalMetric signalMetric : listOfSignalMetric){
			if (signalMetric.signalMetricType == signalTypeMetric){
				return signalMetric;
			}
		}
		
		return null;
	}
	
	public SignalPoint getSignalPointMajority(boolean havePosition, PositionType positionType){
		SignalPoint signalPoint = SignalPoint.none;

		for (SignalMetric signalMetric : listOfSignalMetric){signalMetric.getSignalPoint(havePosition, positionType).occurences++;}
		
		for (SignalMetric signalMetric : listOfSignalMetric){
			if (signalMetric.getSignalPoint(havePosition, positionType) != SignalPoint.none){ //signalMetric.getSignalPoint(havePosition, positionType).occurences > occurences &&
				signalPoint = signalMetric.getSignalPoint(havePosition, positionType);
//				Co.println("--> Have signal at: " + signalMetric.signalMetricType.name() + ", " + signalMetric.strength + ", " + signalPoint.name());
			}
		}
		
		currentSignalPoint = signalPoint;
		return signalPoint;
	}
	
	public ArrayList<SignalMetric> getListOfSignalMetric(){
		return this.listOfSignalMetric;
	}
	
	public void reset(){
		listOfSignalMetric.clear();
	}
	
	public static class CombinedSignal {
		public int longEntry = 0;
		public int longExit = 0;
		public int shortEntry = 0;
		public int shortExit = 0;
		public double strength = 0;
		
		public CombinedSignal(){
			
		}
		
		public CombinedSignal(int longEntry, int longExit, int shortEntry, int shortExit, double strength) {
			this.longEntry = longEntry;
			this.longExit = longExit;
			this.shortEntry = shortEntry;
			this.shortExit = shortExit;
			this.strength = strength;
		}
	}
}
