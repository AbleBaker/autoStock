/**
 * 
 */
package com.autoStock.signal.extras;

import java.util.ArrayList;

import com.autoStock.signal.extras.EncogFrame.FrameType;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.StringTools;

/**
 * @author Kevin
 *
 */
public class EncogFrame {
	public String description;
	public FrameType frameType;
	public ArrayList<EncogSubframe> listOfSubframe = new ArrayList<EncogSubframe>();
	
	public EncogFrame(String description, FrameType frameType, ArrayList<EncogSubframe> listOfSubframe) {
		this.description = description;
		this.frameType = frameType;
		if (listOfSubframe != null){this.listOfSubframe.addAll(listOfSubframe);}
	}
	
	public EncogFrame(String description, FrameType frameType, EncogSubframe encogSubframe) {
		this.description = description;
		this.frameType = frameType;
		listOfSubframe.add(encogSubframe);
	}
	
	public EncogFrame(String description, FrameType frameType) {
		this(description, frameType, new ArrayList<EncogSubframe>());
	}

	public static enum FrameType {
		raw,
		delta_change,
		percent_change,
		category,
		none
	}

	public void addSubframe(EncogSubframe subFrame) {
		if (subFrame.frameType != frameType){throw new IllegalArgumentException("Can't have sub-frame / frames of differing types.");}
		listOfSubframe.add(subFrame);
	}

	public ArrayList<Double> asDoubleList() {
		ArrayList<Double> listOfDouble = new ArrayList<Double>();
		
		for (EncogSubframe subFrame : listOfSubframe){
			listOfDouble.addAll(subFrame.asDoubleList());
		}
		
		return listOfDouble;
	}
	
	public ArrayList<Double> asNormalizedDoubleList(){
		ArrayList<Double> listOfDouble = new ArrayList<Double>();
		
		for (EncogSubframe subFrame : listOfSubframe){
			listOfDouble.addAll(subFrame.asNormalizedDoubleList());
		}
		
		return listOfDouble;
	}
	
	public double[] asDoubleArray() {
		return ArrayTools.getDoubleArray(asDoubleList());
	}
	
	public int getLength(){
		int length = 0;
		
		for (EncogSubframe subFrame : listOfSubframe){
			length += subFrame.asDoubleArray().length;
		}
		
		return length;
	}
	
	public int getSubframeCount(){
		return listOfSubframe.size();
	}
	
	@Override
	public String toString() {
		String string = "EncogFrame: " + frameType.name() + " with " + listOfSubframe.size() + " sub frames";
		
		for (EncogSubframe frame : listOfSubframe){
			string += frame.frameType + " - " + StringTools.arrayOfDoubleToString(frame.asDoubleArray());
		}
		
		return string;
	}
}
