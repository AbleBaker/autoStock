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
	
	public double getChangeFromPeak(double[] arrayOfDouble){
		double max = MathTools.getMax(arrayOfDouble);
		return max - arrayOfDouble[arrayOfDouble.length-1];
	}
	
	public double getChangeFromTrough(double[] arrayOfDouble){
		return 0;
	}
}
