package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.guage.GuageOfThresholdLeft;
import com.autoStock.guage.GuageOfThresholdMet;
import com.autoStock.guage.SignalGuage;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.tools.ArrayTools;
import com.autoStock.types.basic.ImmutableInteger;

public abstract class SignalBase {
	public SignalMetricType signalMetricType = SignalMetricType.none;
	protected ArrayList<Integer> listOfNormalizedValue = new ArrayList<Integer>();
	protected ArrayList<Integer> listOfNormalizedAveragedValue = new ArrayList<Integer>();
	protected ImmutableInteger maxSignalAverage;
	
	public SignalBase(SignalMetricType signalMetricType){
		this.signalMetricType = signalMetricType;
		maxSignalAverage = signalMetricType.maxSignalAverage;
	}
	
	public int getStrength(){
//		Co.println("--> Size: " + getListOfNormalizedValue().size() + ", " +maxSignalAverage.value + this.getClass().getName());

		double normalizedValue = 0;
		int normalizationSize = Math.min(maxSignalAverage.value, listOfNormalizedValue.size());
		
		for (int i=0; i<normalizationSize; i++){
			normalizedValue += listOfNormalizedValue.get(listOfNormalizedValue.size() - i -1);
		}
		
		return (int)normalizedValue / normalizationSize;
//		return getListOfNormalizedValue().get(getListOfNormalizedValue().size()-1);
	}
	
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType){
		if (havePosition == false){
			boolean isQualified = false;
			
			SignalGuage signalGuageForLongEntry = signalMetricType.arrayOfSignalGuageForLongEntry[0];
			SignalGuageType signalGuageTypeForLongEntry = signalGuageForLongEntry.immutableEnumForSignalGuageType.enumValue;
			
			SignalGuage signalGuageForShortEntry = signalMetricType.arrayOfSignalGuageForShortEntry[0];
			SignalGuageType signalGuageTypeForShortEntry = signalGuageForLongEntry.immutableEnumForSignalGuageType.enumValue;
			
			if (signalGuageTypeForLongEntry == SignalGuageType.guage_threshold_met){
				isQualified = new GuageOfThresholdMet(signalGuageForLongEntry, ArrayTools.getArrayFromListOfInt(listOfNormalizedAveragedValue)).isQualified(); 
			}else if (signalGuageTypeForLongEntry == SignalGuageType.guage_threshold_left){
				isQualified = new GuageOfThresholdLeft(signalGuageForLongEntry, ArrayTools.getArrayFromListOfInt(listOfNormalizedAveragedValue)).isQualified();
			}
			
			if (isQualified){
				return new SignalPoint(SignalPointType.long_entry, signalMetricType);
			}
			
			if (signalGuageTypeForShortEntry == SignalGuageType.guage_threshold_met){
				isQualified = new GuageOfThresholdMet(signalGuageForShortEntry, ArrayTools.getArrayFromListOfInt(listOfNormalizedAveragedValue)).isQualified(); 
			}else if (signalGuageTypeForShortEntry == SignalGuageType.guage_threshold_left){
				isQualified = new GuageOfThresholdLeft(signalGuageForShortEntry, ArrayTools.getArrayFromListOfInt(listOfNormalizedAveragedValue)).isQualified();
			}
			
			if (isQualified){
				return new SignalPoint(SignalPointType.short_entry, signalMetricType);
			}
			
		} else{
			if (positionType == PositionType.position_long){
				boolean isQualified = false;
				SignalGuage signalGuageForLongExit = signalMetricType.arrayOfSignalGuageForLongExit[0];
				SignalGuageType signalGuageTypeForLongExit = signalGuageForLongExit.immutableEnumForSignalGuageType.enumValue;
				
				if (signalGuageTypeForLongExit == SignalGuageType.guage_threshold_met){
					isQualified = new GuageOfThresholdMet(signalGuageForLongExit, ArrayTools.getArrayFromListOfInt(listOfNormalizedAveragedValue)).isQualified(); 
				}else if (signalGuageTypeForLongExit == SignalGuageType.guage_threshold_left){
					isQualified = new GuageOfThresholdLeft(signalGuageForLongExit, ArrayTools.getArrayFromListOfInt(listOfNormalizedAveragedValue)).isQualified();
				}
				
				if (isQualified){
					return new SignalPoint(SignalPointType.long_exit, signalMetricType);
				}
			}else if (positionType == PositionType.position_short){
				boolean isQualified = false;
				SignalGuage signalGuageForShortExit = signalMetricType.arrayOfSignalGuageForShortExit[0];
				SignalGuageType signalGuageTypeForShortExit = signalGuageForShortExit.immutableEnumForSignalGuageType.enumValue;
				
				if (signalGuageTypeForShortExit == SignalGuageType.guage_threshold_met){
					isQualified = new GuageOfThresholdMet(signalGuageForShortExit, ArrayTools.getArrayFromListOfInt(listOfNormalizedAveragedValue)).isQualified(); 
				}else if (signalGuageTypeForShortExit == SignalGuageType.guage_threshold_left){
					isQualified = new GuageOfThresholdLeft(signalGuageForShortExit, ArrayTools.getArrayFromListOfInt(listOfNormalizedAveragedValue)).isQualified();
				}
				
				if (isQualified){
					return new SignalPoint(SignalPointType.short_exit, signalMetricType);
				}
			}else{
//				throw new IllegalStateException("PositionType: " + positionType.name());
			}
		}
		
		return new SignalPoint();
	}
	
	public void addInput(double value){
		listOfNormalizedValue.add(signalMetricType.normalizeInterface.normalize(value));
		listOfNormalizedAveragedValue.add(getStrength());
	}
	
	public void prune(int toLength){
		if (listOfNormalizedValue.size() >= toLength){
			listOfNormalizedValue.remove(0);
		}
	}
}
