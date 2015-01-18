/**
 * 
 */
package com.autoStock.signal.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

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
	
	public EncogSubframe(double[] values, FrameType frameType) {
		this.values = values;
		this.frameType = frameType;
		
		normalizer.setActualHigh(MathTools.getMax(values));
		normalizer.setActualLow(MathTools.getMin(values));
	}
	
	public EncogSubframe(ArrayList<Double> values, FrameType frameType) {
		this(ArrayTools.getDoubleArray(values), frameType);
	}
	
	public double[] asDoubleArray(){
		return values;
	}
	
	public double[] asNormalizedDoubleArray(){
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
}
