package com.autoStock.analysis;

import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class PeakDetection {
	private double[] values;
	private double threshold;
	
	public PeakDetection(double[] values){
		this.values = values;
		this.threshold = MathTools.getAverage(values);
	}
	
	public boolean isPeak(){
		for (double value : values){
			
		}
		
		return false;
	}
	
	public boolean isTrough(){
		for (double value : values){
			
		}		
		return false;
	}
}
