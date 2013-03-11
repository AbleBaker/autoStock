/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;

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
	private ArrayList<SignalBase> listOfSignalBase = new ArrayList<SignalBase>();
	
	public Signal(SignalSource signalSource, SignalGroup signalGroup) {
		this.signalSource = signalSource;
		this.signalGroup = signalGroup;
	}
	
	public void addSignalMetrics(ArrayList<SignalMetricType> listOfSignalMetricType){
		for (SignalMetricType signalMetricType : listOfSignalMetricType){
			listOfSignalBase.add(signalGroup.getSignalBaseForType(signalMetricType));
		}
	}
	
	public ArrayList<SignalBase> getListOfSignalBase(){
		return listOfSignalBase;
	}
	
	public SignalGroup getSignalGroup(){
		return signalGroup;
	}
}
