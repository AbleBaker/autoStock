/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPoint;
import com.autoStock.signal.SignalDefinitions.SignalSource;

/**
 * @author Kevin Kowalewski
 *
 */
public class Signal {
	public SignalSource signalSource;
	public SignalPoint currentSignalPoint = new SignalPoint();
	public ArrayList<SignalMetric> listOfSignalMetric = new ArrayList<SignalMetric>();
	
	public Signal(SignalSource signalSource) {
		this.signalSource = signalSource;
	}
	
	public void resetAndAddSignalMetrics(SignalMetric... arrayOfSignalMetrics){
		reset();
		addSignalMetrics(arrayOfSignalMetrics);
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
	
	public ArrayList<SignalMetric> getListOfSignalMetric(){
		return this.listOfSignalMetric;
	}
	
	public SignalMetric[] getArrayOfSignalMetric(){
		return (SignalMetric[]) this.listOfSignalMetric.toArray();
	}
	
	public void reset(){
		listOfSignalMetric.clear();
	}
}
