package com.autoStock.adjust;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentSeriesForAlgorithm extends AdjustmentCampaign {
	private AlgorithmBase algorithmBase; 
	
	public AdjustmentSeriesForAlgorithm(AlgorithmBase algorithmBase){
		super();
		this.algorithmBase = algorithmBase;
	}
	
	@Override
	protected void initializeAdjustmentCampaign() {
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfCCI, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-30, 0, 5)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfCCI, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(0, 30, 5)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfCCI, AdjustmentType.signal_metric_short_entry, new IterableOfInteger(0, 30, 5)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfCCI, AdjustmentType.signal_metric_short_exit, new IterableOfInteger(-30, 0, 5)));
//		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("CCI Period", algorithmBase.signalGroup.signalOfCCI.signalParameters.periodLength, new IterableOfInteger(10, 40, 2)));
//		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("CCI Average", SignalMetricType.metric_cci.maxSignalAverage, new IterableOfInteger(1, 10, 1)));
		
//		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("intervalForReentryMins", algorithmBase.strategyBase.strategyOptions.intervalForReentryMins, new IterableOfInteger(1, 10, 1)));
//		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("maxReenterTimes", algorithmBase.strategyBase.strategyOptions.maxReenterTimes, new IterableOfInteger(1, 10, 1)));
//		listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("maxStopLossPercent", algorithmBase.strategyBase.strategyOptions.maxStopLossPercent, new IterableOfDouble(-10, 0, 0.1)));
//		listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("maxProfitDrawdownPercent", algorithmBase.strategyBase.strategyOptions.maxProfitDrawdownPercent, new IterableOfDouble(-1, 0, 0.1)));

//		listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("minReentryPercentGain", algorithmBase.strategyBase.strategyOptions.minReentryPercentGain, new IterableOfDouble(0.1, 0.5, 0.1)));
		
//		Co.println("--> " + algorithmBase.strategyBase.strategyOptions.maxStopLossPercent.toString());
		
//		Co.println("--> Check: " + algorithmBase.signalGroup.signalOfCCI.signalParameters.periodLength.toString());
	} 	
}
