package com.autoStock.signal;

import java.util.ArrayList;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalTools {
	public static synchronized CombinedSignal getCombinedSignal(Signal signal){
		ArrayList<SignalMetric> listOfSignalMetric = signal.listOfSignalMetric;
		CombinedSignal combinedSignal = new CombinedSignal();
		
		for (SignalMetric signalMetric : listOfSignalMetric){
			combinedSignal.strength += signalMetric.strength;
			combinedSignal.longEntry += signalMetric.signalMetricType.pointToSignalLongEntry;
			combinedSignal.longExit += signalMetric.signalMetricType.pointToSignalLongExit;
			combinedSignal.shortEntry += signalMetric.signalMetricType.pointToSignalShortExit;
			combinedSignal.shortExit += signalMetric.signalMetricType.pointToSignalShortExit;
		}
		
		if (listOfSignalMetric.size() > 0){
			combinedSignal.strength = (combinedSignal.strength / listOfSignalMetric.size());
			combinedSignal.longEntry = (combinedSignal.longEntry / listOfSignalMetric.size());
			combinedSignal.longExit = (combinedSignal.longExit / listOfSignalMetric.size());
			combinedSignal.shortEntry = (combinedSignal.shortEntry / listOfSignalMetric.size());
			combinedSignal.shortExit = (combinedSignal.shortExit / listOfSignalMetric.size());
		}
		
		combinedSignal.strength = (int) (combinedSignal.strength > 0 ? Math.min(100, combinedSignal.strength) : Math.max(-100, combinedSignal.strength));
		
		return combinedSignal;
	}
}
