package com.autoStock.signal.evaluation;

import java.util.ArrayList;
import java.util.Arrays;

import com.autoStock.Co;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.MathTools;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class EvaluationOfCCI extends EvaulationBase {
	private DetectorTools detectorTools = new DetectorTools();
	private ArrayList<QuoteSlice> listOfQuoteSlice;
	private int[] arrayOfNormalizedCCI;
	private int peakDetectWindow = 12;
	
	public EvaluationOfCCI(double[] arrayOfNormalizedCCI){
		this.listOfQuoteSlice = listOfQuoteSlice;
		this.arrayOfNormalizedCCI =  ArrayTools.convertToInt(arrayOfNormalizedCCI);
		
		if (this.arrayOfNormalizedCCI.length >= peakDetectWindow){
			this.arrayOfNormalizedCCI = Arrays.copyOfRange(this.arrayOfNormalizedCCI, arrayOfNormalizedCCI.length-peakDetectWindow, arrayOfNormalizedCCI.length);
		}
	}

	@Override
	public SignalPoint getSignalPoint() {
		
		if (arrayOfNormalizedCCI.length == peakDetectWindow){
			double changeFromPeak = detectorTools.getChangeFromPeak(arrayOfNormalizedCCI);
			double changeFromTrough = detectorTools.getChangeFromTrough(arrayOfNormalizedCCI);
			
//			boolean hasPeaked = arrayOfNormalizedCCI[peakDetectWindow-1] > arrayOfNormalizedCCI[0] && arrayOfNormalizedCCI[arrayOfNormalizedCCI.length-1] < arrayOfNormalizedCCI[arrayOfNormalizedCCI.length-2];
//			boolean hasTroughed = arrayOfNormalizedCCI[peakDetectWindow-1] < arrayOfNormalizedCCI[0] && arrayOfNormalizedCCI[arrayOfNormalizedCCI.length-1] > arrayOfNormalizedCCI[arrayOfNormalizedCCI.length-2];
			
//			hasTroughed = hasTroughed && changeFromTrough < -10;
//			hasPeaked = hasPeaked && changeFromPeak > 10;
			
			int peakValue = MathTools.getMax(arrayOfNormalizedCCI);
			int peakIndex = ArrayTools.getIndex(arrayOfNormalizedCCI, peakValue);
			int troughValue = MathTools.getMin(arrayOfNormalizedCCI);
			int troughIndex = ArrayTools.getIndex(arrayOfNormalizedCCI, troughValue);
			
			double leftOfPeakAverage = peakIndex == 0 ? peakValue : MathTools.getAverage(Arrays.copyOfRange(arrayOfNormalizedCCI, 0, peakIndex));
			double rightOfPeakAverage = peakIndex == arrayOfNormalizedCCI.length-1 ? peakValue : MathTools.getAverage(Arrays.copyOfRange(arrayOfNormalizedCCI, peakIndex+1, arrayOfNormalizedCCI.length-1));
			
			double leftOfTroughAverage = troughIndex == 0 ? troughValue : MathTools.getAverage(Arrays.copyOfRange(arrayOfNormalizedCCI, 0, troughIndex));
			double rightOfTroughAverage = troughIndex == arrayOfNormalizedCCI.length-1 ? troughValue : MathTools.getAverage(Arrays.copyOfRange(arrayOfNormalizedCCI, troughIndex+1, arrayOfNormalizedCCI.length-1));
			
			boolean hasPeaked = leftOfPeakAverage > rightOfPeakAverage;
			boolean hasTroughed = leftOfTroughAverage < rightOfTroughAverage;
			
			Co.println("\n\n --> Values...");
			Co.println("--> Peak, trough index: " + peakIndex + ", " + troughIndex);
			Co.println("--> Left, Right average: " + leftOfPeakAverage + ", " + rightOfPeakAverage);
			
			for (int cciValue : arrayOfNormalizedCCI){
				if (cciValue == peakValue){
					Co.println("--> PEAK CCIValue: " + cciValue);	
				}else if (cciValue == troughValue ){
					Co.println("--> TROUGH CCIValue: " + cciValue);
				}else{
					Co.println("--> CCIValue: " + cciValue);
				}
			}
			
			boolean isAtPeak = changeFromPeak == 0;
			boolean isAtTrough = changeFromTrough == 0;
		
//			Co.println("\n\n--> Evaluation... " + arrayOfNormalizedCCI.length);
//			Co.println("--> Normalized CCI values");
//			for (double cciValue : arrayOfNormalizedCCI){
//				Co.println("--> CCIValue: " + cciValue);
//			}
//			

//			
//			Co.println("--> Has peaked " + hasPeaked);
			
			if (hasPeaked && isAtPeak == false && isAtTrough == false){
				Co.println("--> ******** SIGNAL WAS EXIT");
				
				Co.println("--> Distance from peak is: " + changeFromPeak);
				Co.println("--> Distance from trough is: " + changeFromTrough);
				Co.println("--> isAtPeak, isAtTrough: " + isAtPeak + ", " + isAtTrough);
				
				return new SignalPoint(SignalPointType.long_exit, SignalMetricType.metric_cci);	
			}else if (hasTroughed && isAtTrough == false && isAtPeak == false){
				Co.println("--> ******** SIGNAL WAS ENTRY");
				return new SignalPoint(SignalPointType.long_entry, SignalMetricType.metric_cci);
			}
		}
		
		
		
//		Co.println("--> Is peak, trough: " + isPeaking + ", " + isTroughing);
		
		
		return new SignalPoint();
	}

}
