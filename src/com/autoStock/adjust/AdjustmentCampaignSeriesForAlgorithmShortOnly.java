package com.autoStock.adjust;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentCampaignSeriesForAlgorithmShortOnly extends AdjustmentCampaign {

	@Override
	protected void initializeAdjustmentCampaign() {
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_cci, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-30, 0, 10)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_cci, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(0, 30, 10)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_cci, AdjustmentType.signal_metric_short_entry, new IterableOfInteger(0, 10, 10)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_cci, AdjustmentType.signal_metric_short_exit, new IterableOfInteger(-10, 0, 10)));
	} 	
}
