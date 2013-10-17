package com.autoStock.backtest.watchmaker;

import java.util.ArrayList;

import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfSignalMetric;
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


		return listOfAdjustmentBase;
	}
}
