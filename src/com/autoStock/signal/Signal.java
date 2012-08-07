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
	private ArrayList<SignalMetric> listOfSignalMetric = new ArrayList<SignalMetric>();
	
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
		int occurences = 0;
		SignalPoint signalPoint = SignalPoint.none;

		for (SignalMetric signalMetric : listOfSignalMetric){signalMetric.getSignalPoint(havePosition, positionType).occurences++;}
		
		for (SignalMetric signalMetric : listOfSignalMetric){
			if (signalMetric.getSignalPoint(havePosition, positionType) != SignalPoint.none){ //signalMetric.getSignalPoint(havePosition, positionType).occurences > occurences &&
				signalPoint = signalMetric.getSignalPoint(havePosition, positionType);
				Co.println("--> Have signal at: " + signalMetric.signalMetricType.name() + ", " + signalMetric.strength + ", " + signalPoint.name());
			}
		}
		
		return signalPoint;
	}
	
	public ArrayList<SignalMetric> getListOfSignalMetric(){
		return this.listOfSignalMetric;
	}
	
	public void reset(){
		listOfSignalMetric.clear();
	}
	
	public CombinedSignal getCombinedSignal(){
		CombinedSignal combinedSignal = new CombinedSignal();
		
		for (SignalMetric signalMetric : listOfSignalMetric){
			combinedSignal.strength += signalMetric.strength;
			combinedSignal.longEntry += signalMetric.signalMetricType.pointToSignalLongEntry;
			combinedSignal.longExit += signalMetric.signalMetricType.pointToSignalLongExit;
			combinedSignal.shortEntry += signalMetric.signalMetricType.pointToSignalShortExit;
			combinedSignal.shortExit += signalMetric.signalMetricType.pointToSignalShortExit;
		}
		
		if (listOfSignalMetric.size() > 0){
			combinedSignal.strength = (combinedSignal.strength / listOfSignalMetric.size());
			combinedSignal.longEntry = (combinedSignal.longEntry / listOfSignalMetric.size());
			combinedSignal.longExit = (combinedSignal.longExit / listOfSignalMetric.size());
			combinedSignal.shortEntry = (combinedSignal.shortEntry / listOfSignalMetric.size());
			combinedSignal.shortExit = (combinedSignal.shortExit / listOfSignalMetric.size());
		}
		
		combinedSignal.strength = (int) (combinedSignal.strength > 0 ? Math.min(100, combinedSignal.strength) : Math.max(-100, combinedSignal.strength));
		
		return combinedSignal;
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
