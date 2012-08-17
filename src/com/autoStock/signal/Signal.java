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
	
	public ArrayList<SignalMetric> getListOfSignalMetric(){
		return this.listOfSignalMetric;
	}
	
	public void reset(){
		listOfSignalMetric.clear();
	}
}
