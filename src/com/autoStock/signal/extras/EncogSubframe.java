/**
 * 
 */
package com.autoStock.signal.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public double[] values;
	public FrameType frameType;
	
	public EncogSubframe(double[] values, FrameType frameType) {
		this.values = values;
		this.frameType = frameType;
		
		//if (MathTools.getMin(values) < SignalOfEncog.NORM_MIN){throw new IllegalStateException("Min is less than min signal range: " + MathTools.getMin(values));}
		//if (MathTools.getMax(values) > SignalOfEncog.NORM_MAX){throw new IllegalStateException("Max is more than min signal range: " + MathTools.getMax(values));}
	}
	
	public EncogSubframe(ArrayList<Double> values, FrameType frameType) {
		this.values = ArrayTools.getDoubleArray(values);
		this.frameType = frameType;
	}
	
	public double[] asDoubleArray(){
		return values;
	}
	
	public ArrayList<Double> asDoubleList(){
		return ListTools.getListFromArray(values);
	}
}
