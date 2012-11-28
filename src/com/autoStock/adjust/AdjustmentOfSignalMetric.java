package com.autoStock.adjust;

import com.autoStock.adjust.AdjustmentCampaign.AdjustmentType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentOfSignalMetric extends AdjustmentBase {
	public final SignalMetricType signalMetricType;
	public final AdjustmentType adjustmentType;
	
	public AdjustmentOfSignalMetric(SignalMetricType signalMetricType, AdjustmentType adjustmentType, IterableBase iterableBase){
		this.iterableBase = iterableBase;
		this.signalMetricType = signalMetricType;
		this.adjustmentType = adjustmentType;
	}

	@Override
	public void applyValue() {
		if (adjustmentType == AdjustmentType.signal_metric_long_entry){
			signalMetricType.pointToSignalLongEntry = ((IterableOfInteger)iterableBase).getInt();
		} else if (adjustmentType == AdjustmentType.signal_metric_long_exit){
			signalMetricType.pointToSignalLongExit = ((IterableOfInteger)iterableBase).getInt();
		} else if (adjustmentType == AdjustmentType.signal_metric_short_entry){
			signalMetricType.pointToSignalShortEntry = ((IterableOfInteger)iterableBase).getInt();
		} else if (adjustmentType == AdjustmentType.signal_metric_short_exit){
			signalMetricType.pointToSignalShortExit = ((IterableOfInteger)iterableBase).getInt();
		}else{
			throw new UnsupportedOperationException();
		}
	}
}
