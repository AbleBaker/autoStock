package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.guage.GuageOfThresholdLeft;
import com.autoStock.guage.GuageOfThresholdMet;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.tools.ArrayTools;

public abstract class SignalBase {
	public SignalMetricType signalMetricType = SignalMetricType.none;
	protected ArrayList<Integer> listOfNormalizedInt = new ArrayList<Integer>();
	
	public SignalBase(SignalMetricType signalMetricType){
		this.signalMetricType = signalMetricType;
	}
	
	public int getStrength(){
		return getListOfNormalizedValue().get(getListOfNormalizedValue().size()-1);
	}
	
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType){
		if (havePosition == false){
			boolean isQualified = false;
			
			if (signalMetricType.arrayOfSignalGuageForLongEntry[0].immutableEnumForSignalGuageType.enumValue == SignalGuageType.guage_threshold_met){
				isQualified = new GuageOfThresholdMet(signalMetricType.arrayOfSignalGuageForLongEntry[0], ArrayTools.getArrayFromListOfInt(getListOfNormalizedValue())).isQualified(); 
			}else if (signalMetricType.arrayOfSignalGuageForLongEntry[0].immutableEnumForSignalGuageType.enumValue == SignalGuageType.guage_threshold_left){
				isQualified = new GuageOfThresholdLeft(signalMetricType.arrayOfSignalGuageForLongEntry[0], ArrayTools.getArrayFromListOfInt(getListOfNormalizedValue())).isQualified();
			}
			
			if (isQualified){
				return new SignalPoint(SignalPointType.long_entry, signalMetricType);
			}
		} else{
			boolean isQualified = false;
			
			if (signalMetricType.arrayOfSignalGuageForLongEntry[0].immutableEnumForSignalGuageType.enumValue == SignalGuageType.guage_threshold_met){
				isQualified = new GuageOfThresholdMet(signalMetricType.arrayOfSignalGuageForLongExit[0], ArrayTools.getArrayFromListOfInt(getListOfNormalizedValue())).isQualified(); 
			}else if (signalMetricType.arrayOfSignalGuageForLongEntry[0].immutableEnumForSignalGuageType.enumValue == SignalGuageType.guage_threshold_left){
				isQualified = new GuageOfThresholdLeft(signalMetricType.arrayOfSignalGuageForLongExit[0], ArrayTools.getArrayFromListOfInt(getListOfNormalizedValue())).isQualified();
			}
			
			if (isQualified){
				return new SignalPoint(SignalPointType.long_exit, signalMetricType);
			}
		}
		
		return new SignalPoint();
	}
	
	public void addInput(double value){
		listOfNormalizedInt.add(signalMetricType.normalizeInterface.normalize(value));
	}
	
	public ArrayList<Integer> getListOfNormalizedValue(){
		return listOfNormalizedInt;
	}
	
	public void prune(int toLength){
		if (listOfNormalizedInt.size() >= toLength){
			listOfNormalizedInt.remove(0);
		}
	}
}
