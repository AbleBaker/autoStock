/**
 * 
 */
package com.autoStock.signal.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import com.autoStock.Co;
import com.autoStock.signal.extras.EncogFrame.FrameType;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.ListTools;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin
 *
 */
public class EncogSubframe {
	private double[] values;
	public FrameType frameType;
	private final NormalizedField normalizer = new NormalizedField(NormalizationAction.Normalize, "Subframe Normalizer", 0, 0, 1, -1);
	private int lastRoundHigh = -1;
	private int lastRoundLow = -1;
	
	public EncogSubframe(double[] values, FrameType frameType) {
		this(values, frameType, 0, 0);
	}
	
	public EncogSubframe(ArrayList<Double> values, FrameType frameType) {
		this(ArrayTools.getDoubleArray(values), frameType, 0, 0);
	}
	
	public EncogSubframe(ArrayList<Double> values, FrameType frameType, double normalizerHigh, double normalizerLow) {
		this(ArrayTools.getDoubleArray(values), frameType, normalizerHigh, normalizerLow);
	}
	
	public EncogSubframe(double[] values, FrameType frameType, double normailzerHigh, double normailzerLow) {
		this.values = values;
		this.frameType = frameType;
		
		if (normailzerHigh == 0 && normailzerLow == 0){
			int roundOutHigh = MathTools.roundOut((int) MathTools.getMax(values), 100);
			int roundOutLow = MathTools.roundOut((int) MathTools.getMin(values), 100);
			
			if (lastRoundHigh != -1 && roundOutHigh != lastRoundHigh){ throw new IllegalStateException("Normalizer will fail!");}else{lastRoundHigh = roundOutHigh;}
			if (lastRoundLow != -1 && roundOutLow != lastRoundLow){ throw new IllegalStateException("Normalizer will fail!");}else{lastRoundLow = roundOutLow;}
		
			normalizer.setActualHigh(Math.max(roundOutHigh, 100));
			normalizer.setActualLow(Math.min(roundOutLow, -100));
		
			if (normalizer.getActualHigh() == normalizer.getActualLow() || normalizer.getActualHigh() <= normalizer.getActualLow()){throw new IllegalStateException("Check normailzer inputs (high/low): " + normalizer.getActualHigh() + ", " + normalizer.getActualLow());}			
		}else{
			normalizer.setActualHigh(normailzerHigh);
			normalizer.setActualLow(normailzerLow);
		}
		
//		Co.println("--> Norm: " + normalizer.getActualHigh() + ", " +  normalizer.getActualLow());
//	    Co.println("--> Actual High / Low: " + MathTools.getMax(values) + ", " + MathTools.getMin(values));
	}
	
	public double[] asDoubleArray(){
		return values;
	}
	
	public double[] asNormalizedDoubleArray(){
		if (frameType == FrameType.category){
			return values;
		}
		
		double[] normalizedValues = new double[values.length];
		
		for (int i=0; i<normalizedValues.length; i++){
			normalizedValues[i] = normalizer.normalize(values[i]);
		}
		
		return normalizedValues;
	}
	
	public ArrayList<Double> asDoubleList(){
		return ListTools.getListFromArray(values);
	}
	
	public ArrayList<Double> asNormalizedDoubleList(){
		return ListTools.getListFromArray(asNormalizedDoubleArray());
	}

	public void replaceNaN(){
		for (int i=0; i<values.length; i++){
			if (Double.isNaN(values[i])){
				values[i] = 0;
			}
		}
	}
	
	public boolean isAllZeros(){
		for (Double value : values){
			if (value != 0){
				return false;
			}
		}
		
		return true;
	}
}
