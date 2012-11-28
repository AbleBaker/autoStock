/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCCI{
	private SignalMetricType signalMetricType = SignalMetricType.metric_cci;
	private ArrayList<Double> listOfDouble = new ArrayList<Double>();
	private ArrayList<Double> listOfNormalizedDouble = new ArrayList<Double>();
	
	public void addInput(double cciValue){
		listOfDouble.add(new Double(cciValue));
		listOfNormalizedDouble.add(new Double(signalMetricType.normalizeInterface.normalize(cciValue)));
	}
	
	public ArrayList<Double> getListOfValue(){
		return listOfDouble;
	}
	
	public ArrayList<Double> getListOfNormalizedValue(){
		return listOfNormalizedDouble;
	}
	
	public ArrayList<Double> getListOfNormalizedAveragedValue(){
		ArrayList<Double> listOfAveragedDouble = new ArrayList<Double>();
		
		for (int i = 0; i < listOfNormalizedDouble.size(); i++){
			double averaged = 0;
			
			if (listOfAveragedDouble.size() > 1){
				averaged = (listOfNormalizedDouble.get(i) + listOfNormalizedDouble.get(i-1)) / 2;
			}else{
				averaged = listOfNormalizedDouble.get(i);
			}
			
			listOfAveragedDouble.add(averaged);
		}
		
		return listOfAveragedDouble;
	}
	
	public SignalMetric getSignal(){
		double averaged = 0;
		if (listOfNormalizedDouble.size() > 1){
			 averaged = (listOfNormalizedDouble.get(listOfNormalizedDouble.size()-1).doubleValue() + listOfNormalizedDouble.get(listOfNormalizedDouble.size()-2)) / 2;
		}else{
			averaged = listOfNormalizedDouble.get(listOfNormalizedDouble.size()-1).doubleValue();
		}
				
		return new SignalMetric((int) averaged, signalMetricType);
	}
}
