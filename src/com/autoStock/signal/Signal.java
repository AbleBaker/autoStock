/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.position.PositionDefinitions.PositionType;
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
	private ArrayList<SignalMoment> listOfSignalMoment = new ArrayList<SignalMoment>();
	
	public Signal(SignalSource signalSource, SignalGroup signalGroup) {
		this.signalSource = signalSource;
		this.signalGroup = signalGroup;
	}
	
	public void addSignalBaseFromMetrics(ArrayList<SignalMetricType> listOfSignalMetricType){
		for (SignalMetricType signalMetricType : listOfSignalMetricType){
			listOfSignalBase.add(signalGroup.getSignalBaseForType(signalMetricType));
		}
	}
	
	public void generateSignalMoments(boolean havePosition, PositionType positionType){
		for (SignalBase signalBase : listOfSignalBase){
			String debug = "";
//			if (signalBase instanceof SignalOfEncog){
//				if (((SignalOfEncog)signalBase).getInputWindowRounded() != null && ((SignalOfEncog)signalBase).getInputWindowRounded().length > 0){
//					debug = PrintTools.getString((((SignalOfEncog)signalBase).getInputWindowRounded()));
//					debug += " / " + ((SignalOfEncog)signalBase).getInputWindow().debug;
//				}
//			}
			listOfSignalMoment.add(new SignalMoment(signalBase.signalMetricType, signalBase.getStrength(), signalBase.getSignalPoint(havePosition, positionType), debug));
		}
	}
	
	public ArrayList<SignalBase> getListOfSignalBase(){
		return listOfSignalBase;
	}
	
	public SignalGroup getSignalGroup(){
		return signalGroup;
	}

	public ArrayList<SignalMoment> getListOfSignalMoment() {
		return listOfSignalMoment;
	}
}
