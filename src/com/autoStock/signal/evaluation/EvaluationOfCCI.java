package com.autoStock.signal.evaluation;

import java.util.ArrayList;

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
	
	public EvaluationOfCCI(ArrayList<QuoteSlice> listOfQuoteSlice, double[] arrayOfNormalizedCCI){
		this.listOfQuoteSlice = listOfQuoteSlice;
		this.arrayOfNormalizedCCI = arrayOfNormalizedCCI;
	}

	@Override
	public SignalPoint getSignalPoint() {
		Co.println("--> Distance from peak is: " + detectorTools.getChangeFromPeak(arrayOfNormalizedCCI));
		Co.println("--> Distance from trough is: " + detectorTools.getChangeFromTrough(arrayOfNormalizedCCI));
		
		return new SignalPoint(SignalPointType.long_entry, SignalMetricType.metric_cci);
	}

}
