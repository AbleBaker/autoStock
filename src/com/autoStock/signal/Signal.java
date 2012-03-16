/**
 * 
 */
package com.autoStock.signal;

import java.sql.Date;
import java.util.ArrayList;

import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;

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
	
	public int getCombinedSignal(){
		double combinedSignal = 0;
		
		for (SignalMetric signalMetric : listOfSignalMetric){
			combinedSignal += (signalMetric.strength * SignalDefinitions.getSignalWeight(signalMetric.signalTypeMetric));
		}
		
		combinedSignal = (combinedSignal / listOfSignalMetric.size());
		
		return (int) (combinedSignal > 0 ? Math.min(100, combinedSignal) : Math.max(-100, combinedSignal));
	}
}
