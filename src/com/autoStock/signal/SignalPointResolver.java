package com.autoStock.signal;

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
		if (havePosition == false){
			SignalGuage signalGuageForLongEntry = signalBase.signalParameters.arrayOfSignalGuageForLongEntry[0];
			SignalGuage signalGuageForShortEntry = signalBase.signalParameters.arrayOfSignalGuageForShortEntry[0];
			
			if (getQualification(signalGuageForLongEntry)){
				return new SignalPoint(SignalPointType.long_entry, signalBase.signalMetricType);
			}
			
			if (getQualification(signalGuageForShortEntry)){
				return new SignalPoint(SignalPointType.short_entry, signalBase.signalMetricType);
			}
			
		} else{
			if (positionType == PositionType.position_long){
				SignalGuage signalGuageForLongExit = signalBase.signalParameters.arrayOfSignalGuageForLongExit[0];
				
				if (getQualification(signalGuageForLongExit)){
					return new SignalPoint(SignalPointType.long_exit, signalBase.signalMetricType);
				}
				
			}else if (positionType == PositionType.position_short){
				SignalGuage signalGuageForShortExit = signalBase.signalParameters.arrayOfSignalGuageForShortExit[0];
				
				if (getQualification(signalGuageForShortExit)){
					return new SignalPoint(SignalPointType.short_exit, signalBase.signalMetricType);
				}
			}else{
				//throw new IllegalStateException("PositionType: " + positionType.name());
			}
		}
		
		return new SignalPoint();
	}
	
	private boolean getQualification(SignalGuage signalGuage){
		boolean isQualified = false;
		if (signalGuage.mutableEnumForSignalGuageType.enumValue == SignalGuageType.guage_threshold_met){
			isQualified = new GuageOfThresholdMet(signalGuage, ArrayTools.convertToDouble(ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue))).isQualified(); 
		}else if (signalGuage.mutableEnumForSignalGuageType.enumValue == SignalGuageType.guage_threshold_left){
			isQualified = new GuageOfThresholdLeft(signalGuage, ArrayTools.convertToDouble(ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue))).isQualified();
		}else if (signalGuage.mutableEnumForSignalGuageType.enumValue == SignalGuageType.guage_peak){
			isQualified = new GuageOfPeakAndTrough(signalGuage, ArrayTools.convertToDouble(ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue))).isQualified();
		}else if (signalGuage.mutableEnumForSignalGuageType.enumValue == SignalGuageType.guage_trough){
			isQualified = new GuageOfPeakAndTrough(signalGuage, ArrayTools.convertToDouble(ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue))).isQualified();
		}else if (signalGuage.mutableEnumForSignalGuageType.enumValue == SignalGuageType.none){
			return false;
		}else {
			throw new UnsupportedOperationException("No guage matched: " + signalGuage.mutableEnumForSignalGuageType.enumValue.name());
		}
		
		return isQualified;
	}
}
