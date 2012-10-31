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
	private int peakDetectWindow = 20;
	
	public EvaluationOfCCI(double[] arrayOfNormalizedCCI){
		this.listOfQuoteSlice = listOfQuoteSlice;
		this.arrayOfNormalizedCCI =  arrayOfNormalizedCCI; 
		
		if (this.arrayOfNormalizedCCI.length >= peakDetectWindow){
			this.arrayOfNormalizedCCI = Arrays.copyOfRange(this.arrayOfNormalizedCCI, arrayOfNormalizedCCI.length-peakDetectWindow, arrayOfNormalizedCCI.length);
		}
	}

	@Override
	public SignalPoint getSignalPoint() {
		
		if (arrayOfNormalizedCCI.length == peakDetectWindow){
			double changeFromPeak = detectorTools.getChangeFromPeak(arrayOfNormalizedCCI);
			double changeFromTrough = detectorTools.getChangeFromTrough(arrayOfNormalizedCCI);
			
			boolean hasPeaked = arrayOfNormalizedCCI[peakDetectWindow-1] > arrayOfNormalizedCCI[0];
			boolean hasTroughed = arrayOfNormalizedCCI[peakDetectWindow-1] < arrayOfNormalizedCCI[0] && arrayOfNormalizedCCI[peakDetectWindow-1] > arrayOfNormalizedCCI[peakDetectWindow-2];
			
			boolean isAtPeak = changeFromPeak == 0;
			boolean isAtTrough = changeFromTrough == 0;
		
			Co.println("\n\n--> Evaluation... " + arrayOfNormalizedCCI.length);
			Co.println("--> Normalized CCI values");
			for (double cciValue : arrayOfNormalizedCCI){
				Co.println("--> CCIValue: " + cciValue);
			}
			
			Co.println("--> Distance from peak is: " + changeFromPeak);
			Co.println("--> Distance from trough is: " + changeFromTrough);
			
			Co.println("--> Has peaked " + hasPeaked);
			
			if (hasPeaked && isAtPeak == false && changeFromPeak > 5){
				Co.println("--> ******** SIGNAL WAS EXIT");
				return new SignalPoint(SignalPointType.long_exit, SignalMetricType.metric_cci);
			}else if (hasTroughed && isAtTrough == false && changeFromTrough > -5){
				Co.println("--> ******** SIGNAL WAS ENTRY");
				return new SignalPoint(SignalPointType.long_entry, SignalMetricType.metric_cci);
			}
		}
		
		
		
//		Co.println("--> Is peak, trough: " + isPeaking + ", " + isTroughing);
		
		
		return new SignalPoint();
	}

}
