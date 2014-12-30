/**
 * 
 */
package com.autoStock.signal.extras;

import java.util.ArrayList;

import com.autoStock.signal.extras.EncogFrame.FrameType;

/**
 * @author Kevin
 *
 */
public class EncogFrame {
	public String description;
	public FrameType frameType;
	public ArrayList<EncogSubframe> listOfSubframe = new ArrayList<EncogSubframe>();
	
	public EncogFrame(String description, FrameType frameType) {
		this.description = description;
		this.frameType = frameType;
	}

	public static enum FrameType {
		raw,
		delta_change,
		percent_change,
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
}
