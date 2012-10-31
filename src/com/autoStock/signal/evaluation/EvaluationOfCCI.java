package com.autoStock.signal.evaluation;

import java.util.ArrayList;
import java.util.Arrays;

import com.autoStock.Co;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class EvaluationOfCCI extends EvaulationBase {
	private DetectorTools detectorTools = new DetectorTools();
	private ArrayList<QuoteSlice> listOfQuoteSlice;
	private double[] arrayOfNormalizedCCI;
	private int peakDetectWindow = 15;
	
	public EvaluationOfCCI(double[] arrayOfNormalizedCCI){
		this.listOfQuoteSlice = listOfQuoteSlice;
		this.arrayOfNormalizedCCI =  arrayOfNormalizedCCI; 
		
		if (this.arrayOfNormalizedCCI.length > peakDetectWindow){
			this.arrayOfNormalizedCCI = Arrays.copyOfRange(this.arrayOfNormalizedCCI, arrayOfNormalizedCCI.length-peakDetectWindow, arrayOfNormalizedCCI.length);
		}
	}

	@Override
	public SignalPoint getSignalPoint() {
//		Co.println("--> Evaluation... " + arrayOfNormalizedCCI.length);
//		Co.println("--> Distance from peak is: " + detectorTools.getChangeFromPeak(arrayOfNormalizedCCI));
//		Co.println("--> Distance from trough is: " + detectorTools.getChangeFromTrough(arrayOfNormalizedCCI));
		
		boolean isPeaking = detectorTools.getChangeFromPeak(arrayOfNormalizedCCI) == 0;
		boolean isTroughing = detectorTools.getChangeFromTrough(arrayOfNormalizedCCI) == 0;
		
		if (arrayOfNormalizedCCI.length == peakDetectWindow){
			boolean hasPeaked = arrayOfNormalizedCCI[14] < arrayOfNormalizedCCI[0];
			boolean hasTroughed = arrayOfNormalizedCCI[14] > arrayOfNormalizedCCI[0];
			Co.println("--> Has peaked " + hasPeaked);
			
			if (hasPeaked){
				return new SignalPoint(SignalPointType.long_exit, SignalMetricType.metric_cci);
			}else if (hasTroughed){
				return new SignalPoint(SignalPointType.long_entry, SignalMetricType.metric_cci);
			}
		}
		
		
		
//		Co.println("--> Is peak, trough: " + isPeaking + ", " + isTroughing);
		
		
		return new SignalPoint();
	}

}
