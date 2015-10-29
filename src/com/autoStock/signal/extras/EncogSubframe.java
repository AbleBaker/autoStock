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
import com.autoStock.tools.StringTools;

/**
 * @author Kevin
 *
 */
public class EncogSubframe {
	public final String description;
	private double[] values;
	public FrameType frameType;
	private NormalizedField normalizer;
	
	public EncogSubframe(String description, double[] values, FrameType frameType) {
		this(description, values, frameType, 0, 0);
	}
	
	public EncogSubframe(String description, ArrayList<Double> values, FrameType frameType) {
		this(description, ArrayTools.getDoubleArray(values), frameType, 0, 0);
	}
	
	public EncogSubframe(String description, ArrayList<Double> values, FrameType frameType, double normalizerHigh, double normalizerLow) {
		this(description, ArrayTools.getDoubleArray(values), frameType, normalizerHigh, normalizerLow);
	}
	
	public EncogSubframe(String description, double[] values, FrameType frameType, double normailzerHigh, double normailzerLow) {
		this.description = description;
		this.values = values;
		this.frameType = frameType;
		
		if (normailzerHigh == 0 && normailzerLow == 0){
			if (frameType == FrameType.percent_change || frameType == FrameType.delta_change){
				 normalizer = new NormalizedField(NormalizationAction.Normalize, "Subframe normalize for percents or deltas", 500, -500, 1, -1);
			}else if (frameType == FrameType.category || frameType == FrameType.raw){
				normalizer = new NormalizedField(NormalizationAction.Normalize, "Subframe Normalizer for category", 1, -1, 1, -1);
			}else{
				throw new UnsupportedOperationException();
			}
		}else{
			normalizer = new NormalizedField(NormalizationAction.Normalize, "Subframe Normalizer", normailzerHigh, normailzerLow, 1, -1);
		}
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
	
	public boolean isOutOfRange(){
		for (Double value : frameType == FrameType.category ? values : asNormalizedDoubleArray()){
			if (value < -1 || value > 1){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isAllZeros(){
		for (Double value : values){
			if (value != 0){
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return "Subframe: " + description + ", " + values.length + " -> " + StringTools.arrayOfDoubleToString(values);
	}
}
