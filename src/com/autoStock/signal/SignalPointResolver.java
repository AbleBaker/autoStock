package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.guage.GuageOfPeakAndTrough;
import com.autoStock.guage.GuageOfThresholdLeft;
import com.autoStock.guage.GuageOfThresholdMet;
import com.autoStock.guage.SignalGuage;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.tools.ArrayTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalPointResolver {
	private SignalBase signalBase;
	
	public SignalPointResolver(SignalBase signalBase){
		this.signalBase = signalBase;
	}
	
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType){
		if (signalBase instanceof SignalBaseWithPoint){
			return ((SignalBaseWithPoint)signalBase).getSignalPoint(havePosition, positionType);
		}
		
		if (havePosition == false){
			ArrayList<SignalGuage> listOfSignalGuageForLongEntry = signalBase.signalParameters.getGuagesForType(SignalPointType.long_entry, SignalGuageType.values());
			ArrayList<SignalGuage> listOfSignalGuageForShortEntry = signalBase.signalParameters.getGuagesForType(SignalPointType.short_entry, SignalGuageType.values());
			
			if (allQualified(listOfSignalGuageForLongEntry)){
				return new SignalPoint(SignalPointType.long_entry, signalBase.signalMetricType);
			}
			
			else if (allQualified(listOfSignalGuageForShortEntry)){
				return new SignalPoint(SignalPointType.short_entry, signalBase.signalMetricType);
			}
		}else{
			ArrayList<SignalGuage> listOfSignalGuageForLongExit = signalBase.signalParameters.getGuagesForType(SignalPointType.long_exit, SignalGuageType.values());
			ArrayList<SignalGuage> listOfSignalGuageForShortExit = signalBase.signalParameters.getGuagesForType(SignalPointType.short_exit, SignalGuageType.values());
			
			if (positionType == PositionType.position_long){
				if (allQualified(listOfSignalGuageForLongExit)){
					return new SignalPoint(SignalPointType.long_exit, signalBase.signalMetricType);
				}
			}else if (positionType == PositionType.position_short){
				if (allQualified(listOfSignalGuageForShortExit)){
					return new SignalPoint(SignalPointType.short_exit, signalBase.signalMetricType);
				}
			}
		}
		
		return new SignalPoint();
	}
	
	private boolean allQualified(ArrayList<SignalGuage> listOfSignalGuage){
		if (listOfSignalGuage.size() == 0){
			return false;
		}
		
		for (SignalGuage signalGuage : listOfSignalGuage){
			if (getQualification(signalGuage) == false){
				return false;
			}
		}
		
		return true;
	}
	
	
	private boolean getQualification(SignalGuage signalGuage){
		boolean isQualified = false;
		if (signalGuage.mutableEnumForSignalGuageType.value == SignalGuageType.guage_threshold_met){
			isQualified = new GuageOfThresholdMet(signalBase, signalGuage, ArrayTools.getArrayFromListOfDouble(signalBase.listOfNormalizedAveragedValue)).isQualified(); 
		}else if (signalGuage.mutableEnumForSignalGuageType.value == SignalGuageType.guage_threshold_left){
			isQualified = new GuageOfThresholdLeft(signalBase, signalGuage, ArrayTools.getArrayFromListOfDouble(signalBase.listOfNormalizedAveragedValue)).isQualified();
		}else if (signalGuage.mutableEnumForSignalGuageType.value == SignalGuageType.guage_peak){
			isQualified = new GuageOfPeakAndTrough(signalBase, signalGuage, ArrayTools.getArrayFromListOfDouble(signalBase.listOfNormalizedAveragedValue)).isQualified();
		}else if (signalGuage.mutableEnumForSignalGuageType.value == SignalGuageType.guage_trough){
			isQualified = new GuageOfPeakAndTrough(signalBase, signalGuage, ArrayTools.getArrayFromListOfDouble(signalBase.listOfNormalizedAveragedValue)).isQualified();
		}else if (signalGuage.mutableEnumForSignalGuageType.value == SignalGuageType.none){
			return false;
		}else {
			throw new UnsupportedOperationException("No guage matched: " + signalGuage.mutableEnumForSignalGuageType.value.name());
		}
		
		return isQualified;
	}
}
