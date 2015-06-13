package com.autoStock.signal.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.autoStock.Co;
import com.autoStock.signal.extras.EncogFrame.FrameType;
import com.autoStock.signal.extras.EncogFrameSupport.EncogFrameSource;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.ListTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class EncogInputWindow {
	private ArrayList<EncogFrame> listOfFrame = new ArrayList<EncogFrame>();
	public EncogInputWindow(){}
	
	public double[] getAsWindow(boolean autoNormalized){
		ArrayList<Double> listOfDouble = new ArrayList<Double>();
		
		FrameType frameType = FrameType.none;
		
		for (EncogFrame encogFrame : listOfFrame){
			//Co.println("--> Have frame: " + encogFrame.description + " : " + encogFrame.frameType.name());
			
			if (frameType != FrameType.none && encogFrame.frameType != frameType){
				//Co.println("--> Warning, frame types differ... They should probably be different networks instead: " + encogFrame.description + " : " + frameType + ", " + encogFrame.frameType);
			}
			
			frameType = encogFrame.frameType;
			
			if (autoNormalized){listOfDouble.addAll(encogFrame.asNormalizedDoubleList());}
			else {listOfDouble.addAll(encogFrame.asDoubleList());}
		}
		
		return ArrayTools.getArrayFromListOfDouble(listOfDouble);
	}

	public void addFrame(EncogFrame encogFrame) {
		listOfFrame.add(encogFrame);
	}

	public void addFrames(ArrayList<EncogFrame> encogFrames) {
		listOfFrame.addAll(encogFrames);
	}

	public ArrayList<EncogFrame> getFrames() {
		return listOfFrame;
	}
}