package com.autoStock.indicator;

import java.util.Arrays;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsPTD;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.MathTools;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfPTD extends IndicatorBase {
	private ResultsPTD results;
	private int lastResult = -2;

	public IndicatorOfPTD(MutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore, SignalMetricType signalMetricType) {
		super(periodLength, resultsetLength, commonAnlaysisData, taLibCore, signalMetricType);
	}
	
	public ResultsPTD analyize(int[] arrayOfValues){
		results = new ResultsPTD(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		int result = 0;
		double max = MathTools.getMax(arrayOfValues);
		double min = MathTools.getMin(arrayOfValues);
		double middle = (arrayOfValues[4] + arrayOfValues[5]) / 2;
		double current = arrayOfValues[arrayOfValues.length-1];
		
		boolean isIncreasing = MathTools.isIncreasing(ArrayTools.convertToDouble(Arrays.copyOfRange(arrayOfValues, 6, 10)), 1, false);
		boolean isDecresing = MathTools.isDecreasing(ArrayTools.convertToDouble(Arrays.copyOfRange(arrayOfValues, 6, 10)), 1, false);
		boolean isFlat = MathTools.isFlat(ArrayTools.convertToDouble(Arrays.copyOfRange(arrayOfValues, 6, 10)), 0.5);
		
		double max2 = MathTools.getMax(Arrays.copyOfRange(arrayOfValues, 6, 10));

		for (int i=0; i< arrayOfValues.length; i++){
			Co.print(" " + arrayOfValues[i]);
		}
		
		Co.println("--> Is Decresing: " + isDecresing); 
	
		if (isDecresing){ // && arrayOfValues[9] < middle){
			result = 1;
		}else{
			result = 0;
		}
		
		if (result != lastResult){
			results.arrayOfPTD[0] = result;
			lastResult = result;
			
			if (isDecresing){
				Co.println("--> With values: " + current + ", " + max2);
				int[] values = Arrays.copyOfRange(arrayOfValues, 6, 10);
				
				for (int i=0; i< values.length; i++){
					Co.print(" " + values[i]);
				}
				
				Co.println(" ");
			}
		}else{
			results.arrayOfPTD[0] = 0;
		}
		
//		double leftHalf = MathTools.getAverage(Arrays.copyOfRange(arrayOfValues, 0, 4));
//		double rightHalf = MathTools.getAverage(Arrays.copyOfRange(arrayOfValues, 5, 9));
//		
//		
//		if (leftHalf < rightHalf && rightHalf > min && lastResult != -1){
//			results.arrayOfPTD[0] = -1;
//			lastResult = -1;
//		}else if ((int)leftHalf == (int)rightHalf && lastResult != 0){
//			results.arrayOfPTD[0] = 0;
//			lastResult = 0;
//		}else if (leftHalf > rightHalf && rightHalf < max && lastResult != 1){
//			results.arrayOfPTD[0] = 1;
//			lastResult = 1;
//		}
//		
////		for (int i=0; i<arrayOfValues.length; i++){
////			Co.print(" " + arrayOfValues[i]);
////			results.arrayOfPTD[i] = 0;
////		}
		
		return results;
	}
}
