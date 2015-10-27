package com.autoStock.signal.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.HashUtilities;

import com.autoStock.Co;
import com.autoStock.signal.extras.EncogFrame.FrameType;
import com.autoStock.signal.extras.EncogFrameSupport.EncogFrameSource;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.ListTools;
import com.autoStock.tools.MiscTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class EncogInputWindow {
	private ArrayList<EncogFrame> listOfFrame = new ArrayList<EncogFrame>();
	public EncogInputWindow(){}
	
	public double[] getAsWindow(boolean autoNormalized){
		ArrayList<Double> listOfDouble = new ArrayList<Double>();
		
		for (EncogFrame encogFrame : listOfFrame){
			//Co.println("--> Have frame: " + encogFrame.description + " : " + encogFrame.frameType.name());
			//if (frameType != FrameType.none && encogFrame.frameType != frameType){
			//	//Co.println("--> Warning, frame types differ... They should probably be different networks instead: " + encogFrame.description + " : " + frameType + ", " + encogFrame.frameType);
			//}
			
			ArrayList<Double> frameValues = new ArrayList<Double>();
			
			for (Double value : frameValues){
				if (value > 1 || value < -1){throw new IllegalStateException("Not possible");}
			}
			
			if (autoNormalized){frameValues.addAll(encogFrame.asNormalizedDoubleList());}
			else {frameValues.addAll(encogFrame.asDoubleList());}
			
			listOfDouble.addAll(frameValues);
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

	public int frameCount() {
		return listOfFrame.size();
	}
	
	public String getUniqueIdent(){
		double[] window = getAsWindow(true);
		return listOfFrame.size() + " - " + window.length + " = " + window[0] + "-" + window[1] + "-" + window[2];
	}
	
	public String getHash(){
		return MiscTools.getHash(getUniqueIdent());
	}
}