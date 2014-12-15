package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.indicator.results.ResultsBase;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.tools.ArrayTools;
import com.autoStock.types.basic.MutableInteger;

public abstract class SignalBase {
	public SignalMetricType signalMetricType = SignalMetricType.none;
	protected ArrayList<Double> listOfNormalizedValue = new ArrayList<Double>();
	protected ArrayList<Double> listOfNormalizedAveragedValue = new ArrayList<Double>();
	public ArrayList<Double> listOfNormalizedValuePersist = new ArrayList<Double>();
	protected ArrayList<Double> listOfNormalizedAveragedValuePersist = new ArrayList<Double>();
	protected MutableInteger maxSignalAverage;
	public SignalParameters signalParameters;
	public SignalRangeLimit signalRangeLimit = new SignalRangeLimit();
	
	public SignalBase(SignalMetricType signalMetricType, SignalParameters signalParameters){
		this.signalMetricType = signalMetricType;
		this.signalParameters = signalParameters;
		maxSignalAverage = signalParameters.maxSignalAverage;
	}
	
	public double[] getStrengthWindow(){
		return ArrayTools.getArrayFromListOfDouble(listOfNormalizedAveragedValue);
	}
	
	public double[] getNormalizedAveragedWindow(int windowSize){
		if (windowSize <= 0 || listOfNormalizedAveragedValuePersist.size() - windowSize < 0){throw new IllegalArgumentException();}
		return ArrayTools.getArrayFromListOfDouble(listOfNormalizedAveragedValuePersist.subList(listOfNormalizedAveragedValuePersist.size()-windowSize, listOfNormalizedAveragedValuePersist.size()));
	}
	
	public double[] getNormalizedWindow(int windowSize){
		if (windowSize <= 0 || listOfNormalizedValuePersist.size() - windowSize < 0){throw new IllegalArgumentException("Could not satisfy window size: " + windowSize + ", available: " + listOfNormalizedValuePersist.size());}
		return ArrayTools.getArrayFromListOfDouble(listOfNormalizedValuePersist.subList(listOfNormalizedValuePersist.size()-windowSize, listOfNormalizedValuePersist.size()));
	}

	public double getStrength(){
		if (listOfNormalizedValue.size() == 0){return 0;}
//		Co.println("--> Size: " + listOfNormalizedValue.size() + ", " +maxSignalAverage.value + this.getClass().getName());

		double normalizedValue = 0;
		int normalizationSize = Math.min(maxSignalAverage.value, listOfNormalizedValue.size());
		
		for (int i=0; i<normalizationSize; i++){
			normalizedValue += listOfNormalizedValue.get(listOfNormalizedValue.size() - i -1);
		}
		
		return normalizedValue / normalizationSize;
//		return listOfNormalizedValue.get(listOfNormalizedValue.size()-1);
	}
	
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType){
		return new SignalPointResolver(this).getSignalPoint(havePosition, positionType);
	}
	
	public void setInputCached(double strength, double normalizedValue){
		listOfNormalizedValue.add(normalizedValue);
		listOfNormalizedValuePersist.add(normalizedValue);
		
		listOfNormalizedAveragedValue.add(strength);
		listOfNormalizedAveragedValuePersist.add(strength);
		signalRangeLimit.addValue(strength);
		
		prune(maxSignalAverage.value);
	}
	
	public void setInput(ResultsBase resultsBase){
		setInput(ArrayTools.getLastElement(resultsBase.arrayOfValue));
	}
	
	public void setInput(double value){
		double normalizedValue = signalParameters.normalizeInterface.normalize(value);
		
		listOfNormalizedValue.add(normalizedValue);
		listOfNormalizedValuePersist.add(normalizedValue);
		
		double strength = getStrength();
		
		listOfNormalizedAveragedValue.add(strength);
		listOfNormalizedAveragedValuePersist.add(strength);
		signalRangeLimit.addValue(strength);
		
		prune(maxSignalAverage.value);
	}
	
	private void prune(int toLength){
		while (listOfNormalizedValue.size() > toLength){
			listOfNormalizedValue.remove(0);
		}
		
		while (listOfNormalizedAveragedValue.size() > toLength){
			listOfNormalizedAveragedValue.remove(0);
		}
	}
	
	public double getMiddle(){
		double middle = 0;
		
		for (Double value : listOfNormalizedAveragedValuePersist){
			middle += value;
		}
		
		return middle / listOfNormalizedAveragedValuePersist.size();
	}

	public void reset() {
		listOfNormalizedAveragedValuePersist.clear();
		listOfNormalizedAveragedValue.clear();
		listOfNormalizedValuePersist.clear();
		listOfNormalizedValue.clear();
		signalRangeLimit.reset();
	}
}
