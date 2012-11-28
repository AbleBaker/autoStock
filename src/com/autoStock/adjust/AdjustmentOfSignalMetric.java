package com.autoStock.adjust;

import com.autoStock.adjust.AdjustmentCampaign.AdjustmentType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentOfSignalMetric extends AdjustmentBase {
	public final SignalMetricType signalMetricType;
	
	public AdjustmentOfSignalMetric(SignalMetricType signalMetricType, AdjustmentType adjustmentType, IterableBase iterableBase){
		this.signalMetricType = signalMetricType;
	}

	@Override
	public void applyValue() {
		
	}
}
