package com.autoStock.backtest.watchmaker;

import java.util.ArrayList;

import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentOfBasicDouble;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfEnum;
import com.autoStock.adjust.AdjustmentOfSignalMetric;
import com.autoStock.adjust.IterableOfDouble;
import com.autoStock.adjust.IterableOfEnum;
import com.autoStock.adjust.IterableOfInteger;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentType;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMAdjustmentProvider {
	public ArrayList<AdjustmentBase> getListOfAdjustmentBase(AlgorithmBase algorithmBase){
		ArrayList<AdjustmentBase> listOfAdjustmentBase = new ArrayList<AdjustmentBase>();
		
//		addTypicalSignalRange(listOfAdjustmentBase, algorithmBase.signalGroup.signalOfRSI);
		addTypicalSignalRange(listOfAdjustmentBase, algorithmBase.signalGroup.signalOfUO);
		
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfUO, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-30, 30, 1, false)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfUO, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(-30, 30, 1, false)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfUO, AdjustmentType.signal_metric_short_entry, new IterableOfInteger(-30, 30, 1, false)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(algorithmBase.signalGroup.signalOfUO, AdjustmentType.signal_metric_short_exit, new IterableOfInteger(-30, 30, 1, false)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Period length", algorithmBase.signalGroup.signalOfRSI.signalParameters.periodLength, new IterableOfInteger(20, 60, 1)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Period length", algorithmBase.signalGroup.signalOfUO.signalParameters.periodLength, new IterableOfInteger(20, 60, 1)));

		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("UO Guage Long Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.values()), algorithmBase.signalGroup.signalOfUO.signalParameters.arrayOfSignalGuageForLongEntry[0].mutableEnumForSignalGuageType));
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("UO Guage Long Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.values()), algorithmBase.signalGroup.signalOfUO.signalParameters.arrayOfSignalGuageForLongExit[0].mutableEnumForSignalGuageType));
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("UO Guage Short Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.values()), algorithmBase.signalGroup.signalOfUO.signalParameters.arrayOfSignalGuageForShortEntry[0].mutableEnumForSignalGuageType));
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("UO Guage Long Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.values()), algorithmBase.signalGroup.signalOfUO.signalParameters.arrayOfSignalGuageForShortEntry[0].mutableEnumForSignalGuageType));
     	
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("SO intervalForReentryMins", algorithmBase.strategyBase.strategyOptions.intervalForReentryMins, new IterableOfInteger(0, 5, 1)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("SO intervalForReentryMins", algorithmBase.strategyBase.strategyOptions.intervalForReentryMins, new IterableOfInteger(0, 5, 1)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO maxStopLossPercent", algorithmBase.strategyBase.strategyOptions.maxStopLossPercent, new IterableOfDouble(-0.25, 0, 0.05)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO maxProfitDrawdownPercent", algorithmBase.strategyBase.strategyOptions.maxProfitDrawdownPercent, new IterableOfDouble(-0.25, 0, 0.05)));

		return listOfAdjustmentBase;
	}
	
	private void addTypicalSignalRange(ArrayList<AdjustmentBase> listOfAdjustmentBase, SignalBase signalBase){
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(signalBase, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-30, 30, 1, false)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(signalBase, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(-30, 30, 1, false)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(signalBase, AdjustmentType.signal_metric_short_entry, new IterableOfInteger(-30, 30, 1, false)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(signalBase, AdjustmentType.signal_metric_short_exit, new IterableOfInteger(-30, 30, 1, false)));
	}
}

