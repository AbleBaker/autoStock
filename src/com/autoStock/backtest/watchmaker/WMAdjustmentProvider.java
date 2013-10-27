package com.autoStock.backtest.watchmaker;

import java.util.ArrayList;

import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentOfBasicDouble;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfEnum;
import com.autoStock.adjust.AdjustmentOfSignalMetricThreshold;
import com.autoStock.adjust.IterableOfDouble;
import com.autoStock.adjust.IterableOfEnum;
import com.autoStock.adjust.IterableOfInteger;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentType;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.signal.SignalDefinitions.SignalParametersForUO;
import com.autoStock.signal.SignalPointTacticResolver.SignalPointTactic;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMAdjustmentProvider {
	public ArrayList<AdjustmentBase> getListOfAdjustmentBase(AlgorithmBase algorithmBase){
		ArrayList<AdjustmentBase> listOfAdjustmentBase = new ArrayList<AdjustmentBase>();
		
     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Period length EMA 1", algorithmBase.indicatorGroup.indicatorOfEMAFirst.indicatorParameters.periodLength, new IterableOfInteger(3, 60, 1)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Period length EMA 2", algorithmBase.indicatorGroup.indicatorOfEMASecond.indicatorParameters.periodLength, new IterableOfInteger(3, 60, 1)));
     	
     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("Long gap size", algorithmBase.signalGroup.signalOfCrossover.longGapSize, new IterableOfDouble(3, 60, 1)));
		
//		addTypicalSignalRange(listOfAdjustmentBase, algorithmBase.signalGroup.signalOfUO);
//		addTypicalSignalRange(listOfAdjustmentBase, algorithmBase.signalGroup.signalOfCCI);
//		addTypicalSignalRange(listOfAdjustmentBase, algorithmBase.signalGroup.signalOfRSI);
//		addTypicalSignalRange(listOfAdjustmentBase, algorithmBase.signalGroup.signalOfWILLR);
		
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Period length", algorithmBase.indicatorGroup.indicatorOfUO.indicatorParameters.periodLength, new IterableOfInteger(20, 60, 1)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Period length", algorithmBase.signalGroup.signalOfCCI.signalParameters.periodLength, new IterableOfInteger(20, 60, 1)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Period length", algorithmBase.signalGroup.signalOfRSI.signalParameters.periodLength, new IterableOfInteger(20, 60, 1)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Period length", algorithmBase.indicatorGroup.indicatorOfWILLR.indicatorParameters.periodLength, new IterableOfInteger(20, 60, 1)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Average UO", algorithmBase.signalGroup.signalOfUO.signalParameters.maxSignalAverage, new IterableOfInteger(1, 20, 1)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Average CCI", algorithmBase.signalGroup.signalOfCCI.signalParameters.maxSignalAverage, new IterableOfInteger(1, 20, 1)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Average RSI", algorithmBase.signalGroup.signalOfRSI.signalParameters.maxSignalAverage, new IterableOfInteger(1, 20, 1)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("Average WILLR", algorithmBase.signalGroup.signalOfWILLR.signalParameters.maxSignalAverage, new IterableOfInteger(1, 20, 1)));
//
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("UO Guage Long Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfUO.signalParameters.arrayOfSignalGuageForLongEntry[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("UO Guage Long Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfUO.signalParameters.arrayOfSignalGuageForLongExit[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("UO Guage Short Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfUO.signalParameters.arrayOfSignalGuageForShortEntry[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("UO Guage Short Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfUO.signalParameters.arrayOfSignalGuageForShortExit[0].mutableEnumForSignalGuageType));
     	
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("CCI Guage Long Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfCCI.signalParameters.arrayOfSignalGuageForLongEntry[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("CCI Guage Long Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfCCI.signalParameters.arrayOfSignalGuageForLongExit[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("CCI Guage Short Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfCCI.signalParameters.arrayOfSignalGuageForShortEntry[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("CCI Guage Short Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfCCI.signalParameters.arrayOfSignalGuageForShortExit[0].mutableEnumForSignalGuageType));

//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("RSI Guage Long Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfRSI.signalParameters.arrayOfSignalGuageForLongEntry[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("RSI Guage Long Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfRSI.signalParameters.arrayOfSignalGuageForLongExit[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("RSI Guage Short Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfRSI.signalParameters.arrayOfSignalGuageForShortEntry[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("RSI Guage Short Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfRSI.signalParameters.arrayOfSignalGuageForShortExit[0].mutableEnumForSignalGuageType));
     	
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("WILLR Guage Long Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfWILLR.signalParameters.arrayOfSignalGuageForLongEntry[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("WILLR Guage Long Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfWILLR.signalParameters.arrayOfSignalGuageForLongExit[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("WILLR Guage Short Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfWILLR.signalParameters.arrayOfSignalGuageForShortEntry[0].mutableEnumForSignalGuageType));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>("WILLR Guage Short Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left), algorithmBase.signalGroup.signalOfWILLR.signalParameters.arrayOfSignalGuageForShortExit[0].mutableEnumForSignalGuageType));
     	
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalPointTactic>("SO Tactic Entry", new IterableOfEnum<SignalPointTactic>(SignalPointTactic.tactic_any, SignalPointTactic.tactic_combined), algorithmBase.strategyBase.strategyOptions.signalPointTacticForEntry));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalPointTactic>("SO Tactic Exit", new IterableOfEnum<SignalPointTactic>(SignalPointTactic.tactic_any, SignalPointTactic.tactic_combined), algorithmBase.strategyBase.strategyOptions.signalPointTacticForExit));		
		
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO maxStopLossPercent", algorithmBase.strategyBase.strategyOptions.maxStopLossPercent, new IterableOfDouble(-0.25, 0, 0.01)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO maxProfitDrawdownPercent", algorithmBase.strategyBase.strategyOptions.maxProfitDrawdownPercent, new IterableOfDouble(-0.25, 0, 0.01)));
     	
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("SO intervalForReentryMins", algorithmBase.strategyBase.strategyOptions.intervalForReentryMins, new IterableOfInteger(1, 5, 1)));
//     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO minReentryPercentGain", algorithmBase.strategyBase.strategyOptions.minReentryPercentGain, new IterableOfDouble(0, 0.50, 0.01)));

		return listOfAdjustmentBase;
	}
	
	private void addTypicalSignalRange(ArrayList<AdjustmentBase> listOfAdjustmentBase, SignalBase signalBase){
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetricThreshold(signalBase, AdjustmentType.signal_metric_long_entry, new IterableOfDouble(-30, 30, 1)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetricThreshold(signalBase, AdjustmentType.signal_metric_long_exit, new IterableOfDouble(-30, 30, 1)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetricThreshold(signalBase, AdjustmentType.signal_metric_short_entry, new IterableOfDouble(-30, 30, 1)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetricThreshold(signalBase, AdjustmentType.signal_metric_short_exit, new IterableOfDouble(-30, 30, 1)));
	}
}

