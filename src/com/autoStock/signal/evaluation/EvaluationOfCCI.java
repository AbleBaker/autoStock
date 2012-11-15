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
import com.lowagie.text.pdf.codec.CCITTG4Encoder;

/**
 * @author Kevin Kowalewski
 *
 */
public class EvaluationOfCCI extends EvaulationBase {
	private DetectorTools detectorTools = new DetectorTools();
	private int[] arrayOfNormalizedCCI;
	private int detectWindow = 15;
	
	public EvaluationOfCCI(double[] arrayOfNormalizedCCI){
		this.arrayOfNormalizedCCI =  ArrayTools.convertToInt(arrayOfNormalizedCCI);
		
		if (this.arrayOfNormalizedCCI.length >= detectWindow){
			this.arrayOfNormalizedCCI = Arrays.copyOfRange(this.arrayOfNormalizedCCI, arrayOfNormalizedCCI.length-detectWindow, arrayOfNormalizedCCI.length);
		}
	}

	@Override
	public SignalPoint getSignalPoint() {
		if (arrayOfNormalizedCCI.length == detectWindow){
			int currentCCIValue = arrayOfNormalizedCCI[arrayOfNormalizedCCI.length-1];
			int lastCCIValue = arrayOfNormalizedCCI[arrayOfNormalizedCCI.length-2];
			int changeFromPeak = detectorTools.getChangeFromPeak(arrayOfNormalizedCCI);
			int changeFromTrough = detectorTools.getChangeFromTrough(arrayOfNormalizedCCI);
			int changeFromMaxToMin = detectorTools.getChangeBetweenMaxAndMin(arrayOfNormalizedCCI);
			
			int peakValue = MathTools.getMax(arrayOfNormalizedCCI);
			int peakIndex = ArrayTools.getIndex(arrayOfNormalizedCCI, peakValue);
			int troughValue = MathTools.getMin(arrayOfNormalizedCCI);
			int troughIndex = ArrayTools.getIndex(arrayOfNormalizedCCI, troughValue);
			
			double leftOfPeakAverage = peakIndex == 0 ? peakValue : MathTools.getAverage(Arrays.copyOfRange(arrayOfNormalizedCCI, 0, peakIndex));
			double rightOfPeakAverage = peakIndex == arrayOfNormalizedCCI.length-1 ? peakValue : MathTools.getAverage(Arrays.copyOfRange(arrayOfNormalizedCCI, peakIndex+1, arrayOfNormalizedCCI.length));
			
			double leftOfTroughAverage = troughIndex == 0 ? troughValue : MathTools.getAverage(Arrays.copyOfRange(arrayOfNormalizedCCI, 0, troughIndex));
			double rightOfTroughAverage = troughIndex == arrayOfNormalizedCCI.length-1 ? troughValue : MathTools.getAverage(Arrays.copyOfRange(arrayOfNormalizedCCI, troughIndex+1, arrayOfNormalizedCCI.length));
			
			boolean isAtPeak = changeFromPeak == 0;
			boolean isAtTrough = changeFromTrough == 0;
			
			boolean directionSincePeakIsDown = detectorTools.directionIsDown(Arrays.copyOfRange(arrayOfNormalizedCCI, peakIndex, arrayOfNormalizedCCI.length), 3);
			boolean directionSincePeakIsUp = detectorTools.directionIsUp(Arrays.copyOfRange(arrayOfNormalizedCCI, peakIndex, arrayOfNormalizedCCI.length), 3);
			boolean directionSinceTroughIsDown = detectorTools.directionIsDown(Arrays.copyOfRange(arrayOfNormalizedCCI, troughIndex, arrayOfNormalizedCCI.length), 3);
			boolean directionSinceTroughIsUp = detectorTools.directionIsUp(Arrays.copyOfRange(arrayOfNormalizedCCI, troughIndex, arrayOfNormalizedCCI.length), 3);
			
//			Co.println("--> Debug - - - > " + currentCCIValue + " : " + peakValue + ", " + troughValue + ", " + changeFromPeak + ", " + changeFromTrough + ", " + changeFromMaxToMin + ", " + isAtPeak + ", " + isAtTrough);
			
			boolean hasPeaked = changeFromPeak < 0;
			boolean hasTroughed = changeFromTrough > 10 && directionSinceTroughIsUp && changeFromMaxToMin > 10;
			
			if (hasTroughed){
//				Co.println("--> ******** SIGNAL WAS ENTRY");
//				Co.println("--> Values: " + currentCCIValue + "," + troughIndex + ", " + leftOfTroughAverage + ", " + rightOfTroughAverage + ", " + directionSincePeakIsUp);
				return new SignalPoint(SignalPointType.long_entry, SignalMetricType.metric_cci);
			}else if (hasPeaked){
				return new SignalPoint(SignalPointType.long_exit, SignalMetricType.metric_cci);
			}
		}

		return new SignalPoint();
	}

}
