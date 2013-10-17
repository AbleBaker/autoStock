package com.autoStock.backtest.watchmaker;

import java.util.ArrayList;

import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentOfBasicDouble;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfSignalMetric;
import com.autoStock.adjust.IterableOfDouble;
import com.autoStock.adjust.IterableOfInteger;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentType;
import com.autoStock.algorithm.AlgorithmBase;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMAdjustmentProvider {
	public ArrayList<AdjustmentBase> getListOfAdjustmentBase(AlgorithmBase algorithmBase){
		ArrayList<AdjustmentBase> listOfAdjustmentBase = new ArrayList<AdjustmentBase>();
		
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfUO, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-30, 30, 1, false)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfUO, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(-30, 30, 1, false)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfUO, AdjustmentType.signal_metric_short_entry, new IterableOfInteger(-30, 30, 1, false)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfUO, AdjustmentType.signal_metric_short_exit, new IterableOfInteger(-30, 30, 1, false)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("UO Period", algorithmBase.signalGroup.signalOfUO.signalParameters.periodLength, new IterableOfInteger(20, 60, 1)));

     	
     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("SO intervalForReentryMins", algorithmBase.strategyBase.strategyOptions.intervalForReentryMins, new IterableOfInteger(0, 5, 1)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO maxStopLossPercent", algorithmBase.strategyBase.strategyOptions.maxStopLossPercent, new IterableOfDouble(-0.50, 0, 0.01)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO maxProfitDrawdownPercent", algorithmBase.strategyBase.strategyOptions.maxProfitDrawdownPercent, new IterableOfDouble(-0.50, 0, 0.01)));

		return listOfAdjustmentBase;
	}
}

