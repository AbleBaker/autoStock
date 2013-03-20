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
		this.description = signalMetricType.name() + ", " + adjustmentType.name();
		this.signalMetricType = signalMetricType;
		this.adjustmentType = adjustmentType;
	}

	@Override
	public void applyValue() {
		if (adjustmentType == AdjustmentType.signal_metric_long_entry){
			signalMetricType.arrayOfSignalGuageForLongEntry[0].threshold = ((IterableOfInteger)iterableBase).getInt();
		} else if (adjustmentType == AdjustmentType.signal_metric_long_exit){
			signalMetricType.arrayOfSignalGuageForLongExit[0].threshold = ((IterableOfInteger)iterableBase).getInt();
		} else if (adjustmentType == AdjustmentType.signal_metric_short_entry){
			signalMetricType.arrayOfSignalGuageForShortEntry[0].threshold = ((IterableOfInteger)iterableBase).getInt();
		} else if (adjustmentType == AdjustmentType.signal_metric_short_exit){
			signalMetricType.arrayOfSignalGuageForShortExit[0].threshold = ((IterableOfInteger)iterableBase).getInt();
		}else{
			throw new UnsupportedOperationException();
		}
	}
	
	public int getValue(){
		return ((IterableOfInteger)iterableBase).getInt();
	}
}
