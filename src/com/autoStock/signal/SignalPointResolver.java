package com.autoStock.signal;

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
			boolean isQualified = false;
			
			SignalGuage signalGuageForLongEntry = signalBase.signalParameters.arrayOfSignalGuageForLongEntry[0];
			SignalGuageType signalGuageTypeForLongEntry = signalGuageForLongEntry.mutableEnumForSignalGuageType.enumValue;
			
			SignalGuage signalGuageForShortEntry = signalBase.signalParameters.arrayOfSignalGuageForShortEntry[0];
			SignalGuageType signalGuageTypeForShortEntry = signalGuageForShortEntry.mutableEnumForSignalGuageType.enumValue;
						
			if (signalGuageTypeForLongEntry == SignalGuageType.guage_threshold_met){
				isQualified = new GuageOfThresholdMet(signalGuageForLongEntry, ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue)).isQualified(); 
			}else if (signalGuageTypeForLongEntry == SignalGuageType.guage_threshold_left){
				isQualified = new GuageOfThresholdLeft(signalGuageForLongEntry, ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue)).isQualified();
			}
			
			if (isQualified){
				return new SignalPoint(SignalPointType.long_entry, signalBase.signalMetricType);
			}
			
			if (signalGuageTypeForShortEntry == SignalGuageType.guage_threshold_met){
				isQualified = new GuageOfThresholdMet(signalGuageForShortEntry, ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue)).isQualified(); 
			}else if (signalGuageTypeForShortEntry == SignalGuageType.guage_threshold_left){
				isQualified = new GuageOfThresholdLeft(signalGuageForShortEntry, ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue)).isQualified();
			}
			
			if (isQualified){
				return new SignalPoint(SignalPointType.short_entry, signalBase.signalMetricType);
			}
			
		} else{
			if (positionType == PositionType.position_long){
				boolean isQualified = false;
				SignalGuage signalGuageForLongExit = signalBase.signalParameters.arrayOfSignalGuageForLongExit[0];
				SignalGuageType signalGuageTypeForLongExit = signalGuageForLongExit.mutableEnumForSignalGuageType.enumValue;
				
				if (signalGuageTypeForLongExit == SignalGuageType.guage_threshold_met){
					isQualified = new GuageOfThresholdMet(signalGuageForLongExit, ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue)).isQualified(); 
				}else if (signalGuageTypeForLongExit == SignalGuageType.guage_threshold_left){
					isQualified = new GuageOfThresholdLeft(signalGuageForLongExit, ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue)).isQualified();
				}
				
				if (isQualified){
					return new SignalPoint(SignalPointType.long_exit, signalBase.signalMetricType);
				}
			}else if (positionType == PositionType.position_short){
				boolean isQualified = false;
				SignalGuage signalGuageForShortExit = signalBase.signalParameters.arrayOfSignalGuageForShortExit[0];
				SignalGuageType signalGuageTypeForShortExit = signalGuageForShortExit.mutableEnumForSignalGuageType.enumValue;
				
				if (signalGuageTypeForShortExit == SignalGuageType.guage_threshold_met){
					isQualified = new GuageOfThresholdMet(signalGuageForShortExit, ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue)).isQualified(); 
				}else if (signalGuageTypeForShortExit == SignalGuageType.guage_threshold_left){
					isQualified = new GuageOfThresholdLeft(signalGuageForShortExit, ArrayTools.getArrayFromListOfInt(signalBase.listOfNormalizedAveragedValue)).isQualified();
				}
				
				if (isQualified){
					return new SignalPoint(SignalPointType.short_exit, signalBase.signalMetricType);
				}
			}else{
//				throw new IllegalStateException("PositionType: " + positionType.name());
			}
		}
		
		return new SignalPoint();
	}
}
