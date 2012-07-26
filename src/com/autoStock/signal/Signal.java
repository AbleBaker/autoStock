/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;

/**
 * @author Kevin Kowalewski
 *
 */
public class Signal {
	public SignalSource signalSource;
	public SignalType lastSignalType = SignalType.type_none;
	public SignalType currentSignalType = SignalType.type_none;
	private ArrayList<SignalMetric> listOfSignalMetric = new ArrayList<SignalMetric>();
	
	public Signal(SignalSource signalSource) {
		this.signalSource = signalSource;
	}
	
	public void addSignalMetrics(SignalMetric... signalMetrics){
		for (SignalMetric signalMetric : signalMetrics){
			listOfSignalMetric.add(signalMetric);
		}
	}
	
	public SignalMetric getSignalMetric(SignalTypeMetric signalTypeMetric ){
		for (SignalMetric signalMetric : listOfSignalMetric){
			if (signalMetric.signalTypeMetric == signalTypeMetric){
				return signalMetric;
			}
		}
		
		return null;
	}
	
	public void reset(){
		listOfSignalMetric.clear();
	}
	
	public CombinedSignal getCombinedSignal(){
		CombinedSignal combinedSignal = new CombinedSignal();
		
		for (SignalMetric signalMetric : listOfSignalMetric){
			combinedSignal.strength += signalMetric.strength;
			combinedSignal.longEntry += signalMetric.signalTypeMetric.pointToSignalLongEntry;
			combinedSignal.longExit += signalMetric.signalTypeMetric.pointToSignalLongExit;
			combinedSignal.shortEntry += signalMetric.signalTypeMetric.pointToSignalShortExit;
			combinedSignal.shortExit += signalMetric.signalTypeMetric.pointToSignalShortExit;
		}
		
		combinedSignal.strength = (combinedSignal.strength / listOfSignalMetric.size());
		combinedSignal.longEntry = (combinedSignal.longEntry / listOfSignalMetric.size());
		combinedSignal.longExit = (combinedSignal.longExit / listOfSignalMetric.size());
		combinedSignal.shortEntry = (combinedSignal.shortEntry / listOfSignalMetric.size());
		combinedSignal.shortExit = (combinedSignal.shortExit / listOfSignalMetric.size());
		
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
