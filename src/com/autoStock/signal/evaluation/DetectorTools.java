package com.autoStock.signal.evaluation;

import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class DetectorTools {
	public boolean detectPeak(){
		return true;
	}
	
	public boolean detectTrough(){
		return true;
	}
	
	public int getChangeFromPeak(int[] arrayOfInteger){
		int max = MathTools.getMax(arrayOfInteger);
		return max - arrayOfInteger[arrayOfInteger.length-1];
	}
	
	public int getChangeFromTrough(int[] arrayOfInteger){
		int min = MathTools.getMin(arrayOfInteger);
		return min - arrayOfInteger[arrayOfInteger.length-1];
	}
}
