package com.autoStock.adjust;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentCampaignSeriesForAlgorithm extends AdjustmentCampaign {
	private AlgorithmBase algorithmBase; 
	
	public AdjustmentCampaignSeriesForAlgorithm(AlgorithmBase algorithmBase){
		super();
		this.algorithmBase = algorithmBase;
	}
	
	@Override
	protected void initializeAdjustmentCampaign() {
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfCCI, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-30, 0, 2)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfCCI, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(0, 30, 2)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfCCI, AdjustmentType.signal_metric_short_entry, new IterableOfInteger(0, 30, 2)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfCCI, AdjustmentType.signal_metric_short_exit, new IterableOfInteger(-30, 0, 2)));
	} 	
}
