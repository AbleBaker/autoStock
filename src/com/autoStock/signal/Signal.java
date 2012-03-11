/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalType;

/**
 * @author Kevin Kowalewski
 *
 */
public class Signal {
	public SignalSource signalSource;
	public SignalType currentSignalType;
	public ArrayList<SignalMetric> listOfSignalMetric = new ArrayList<SignalMetric>();
	
	public Signal(SignalSource signalSource) {
		this.signalSource = signalSource;
	}
	
	public void addSignalMetrics(SignalMetric... signalMetrics){
		for (SignalMetric signalMetric : signalMetrics){
			listOfSignalMetric.add(signalMetric);
		}
	}
	
	public void reset(){
		listOfSignalMetric.clear();
	}
	
	public int getCombinedSignal(){
		double combinedSignal = 0;
		
		for (SignalMetric signalMetric : listOfSignalMetric){
			combinedSignal += (signalMetric.strength * SignalDefinitions.getSignalWeight(signalMetric.signalTypeMetric));
		}
		
		return (int) combinedSignal;
	}
}
