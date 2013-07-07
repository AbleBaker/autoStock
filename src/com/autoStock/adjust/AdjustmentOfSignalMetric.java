package com.autoStock.adjust;

import com.autoStock.Co;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentType;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentOfSignalMetric extends AdjustmentBase {
	public final SignalBase signalBase;
	public final AdjustmentType adjustmentType;
	
	public AdjustmentOfSignalMetric(SignalBase signalBase, AdjustmentType adjustmentType, IterableBase iterableBase){
		this.iterableBase = iterableBase;
		this.description = signalBase.signalMetricType.name() + ", " + adjustmentType.name(); 
		this.adjustmentType = adjustmentType;
		this.signalBase = signalBase;
	}

	@Override
	public void applyValue() {
		if (adjustmentType == AdjustmentType.signal_metric_long_entry){
			signalBase.signalParameters.arrayOfSignalGuageForLongEntry[0].threshold = ((IterableOfInteger)iterableBase).getInt();
		}else if (adjustmentType == AdjustmentType.signal_metric_long_exit){
			signalBase.signalParameters.arrayOfSignalGuageForLongExit[0].threshold = ((IterableOfInteger)iterableBase).getInt();
		}else if (adjustmentType == AdjustmentType.signal_metric_short_entry){
			signalBase.signalParameters.arrayOfSignalGuageForShortEntry[0].threshold = ((IterableOfInteger)iterableBase).getInt();
		}else if (adjustmentType == AdjustmentType.signal_metric_short_exit){
			signalBase.signalParameters.arrayOfSignalGuageForShortExit[0].threshold = ((IterableOfInteger)iterableBase).getInt();
		}else {
			throw new UnsupportedOperationException("Unknown adjustment type: " + adjustmentType.name());
		}
		
		Co.println("--> Adjusted: " + adjustmentType.name() + ", " + ((IterableOfInteger)iterableBase).getInt());
	}
	
	public int getValue(){
		return ((IterableOfInteger)iterableBase).getInt();
	}
}
