/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalSource;

/**
 * @author Kevin Kowalewski
 *
 */
public class Signal {
	private SignalGroup signalGroup;
	public SignalSource signalSource;
	public SignalPoint currentSignalPoint = new SignalPoint();
	private ArrayList<SignalMetric> listOfSignalMetric = new ArrayList<SignalMetric>();
	
	public Signal(SignalSource signalSource, SignalGroup signalGroup) {
		this.signalSource = signalSource;
		this.signalGroup = signalGroup;
	}
	
	public void addSignalMetrics(ArrayList<SignalMetricType> listOfSignalMetricType){
		for (SignalMetricType signalMetricType : listOfSignalMetricType){
			listOfSignalMetric.add(signalGroup.getSignalMetricForType(signalMetricType));
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
		return listOfSignalMetric;
	}
	
	public SignalGroup getSignalGroup(){
		return signalGroup;
	}
}
