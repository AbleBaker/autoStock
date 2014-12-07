package com.autoStock.backtest.watchmaker;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentOfBasicBoolean;
import com.autoStock.adjust.AdjustmentOfBasicDouble;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfEnum;
import com.autoStock.adjust.AdjustmentOfSignalMetricThreshold;
import com.autoStock.adjust.IterableOfBoolean;
import com.autoStock.adjust.IterableOfDouble;
import com.autoStock.adjust.IterableOfEnum;
import com.autoStock.adjust.IterableOfInteger;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentType;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.signal.SignalDefinitions.SignalParametersForCrossover;
import com.autoStock.signal.SignalDefinitions.SignalParametersForUO;
import com.autoStock.signal.TacticResolver.SignalPointTactic;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMAdjustmentProvider {
	public ArrayList<AdjustmentBase> getListOfAdjustmentBase(AlgorithmBase algorithmBase){
		ArrayList<AdjustmentBase> listOfAdjustmentBase = new ArrayList<AdjustmentBase>();
		
		listOfAdjustmentBase.addAll(new WMAdjustmentGenerator().getTypicalAdjustmentForIndicator(algorithmBase.indicatorGroup.indicatorOfCCI));
		listOfAdjustmentBase.addAll(new WMAdjustmentGenerator().getTypicalAdjustmentForIndicator(algorithmBase.indicatorGroup.indicatorOfUO));
		listOfAdjustmentBase.addAll(new WMAdjustmentGenerator().getTypicalAdjustmentForIndicator(algorithmBase.indicatorGroup.indicatorOfDI));
		new WMAdjustmentGenerator().addCustomIndicatorParameters(algorithmBase.indicatorGroup.indicatorOfWILLR, listOfAdjustmentBase, 25, 60);
		new WMAdjustmentGenerator().addCustomIndicatorParameters(algorithmBase.indicatorGroup.indicatorOfADX, listOfAdjustmentBase, 25, 60);
			
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalPointTactic>("SO Tactic Entry", new IterableOfEnum<SignalPointTactic>(SignalPointTactic.tactic_any, SignalPointTactic.tactic_combined), algorithmBase.strategyBase.strategyOptions.signalPointTacticForEntry));
//		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalPointTactic>("SO Tactic Exit", new IterableOfEnum<SignalPointTactic>(SignalPointTactic.tactic_any, SignalPointTactic.tactic_combined), algorithmBase.strategyBase.strategyOptions.signalPointTacticForExit));
		
		//Long, Short & Reentry
//		listOfAdjustmentBase.add(new AdjustmentOfBasicBoolean("SO canGoLong", algorithmBase.strategyBase.strategyOptions.canGoLong, new IterableOfBoolean()));
//		listOfAdjustmentBase.add(new AdjustmentOfBasicBoolean("SO canGoShort", algorithmBase.strategyBase.strategyOptions.canGoShort, new IterableOfBoolean()));
		listOfAdjustmentBase.add(new AdjustmentOfBasicBoolean("SO canReenter", algorithmBase.strategyBase.strategyOptions.canReenter, new IterableOfBoolean()));
		
		//Stop Loss & Profit Drawdown
		listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO maxStopLossPercent", algorithmBase.strategyBase.strategyOptions.maxStopLossPercent, new IterableOfDouble(-0.25, 0, 0.01)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO maxProfitDrawdownPercent", algorithmBase.strategyBase.strategyOptions.maxProfitDrawdownPercent, new IterableOfDouble(-0.25, 0, 0.01)));
     	
     	//Reentry
     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("SO intervalForReentryMins", algorithmBase.strategyBase.strategyOptions.intervalForReentryMins, new IterableOfInteger(1, 15, 1)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO minReentryPercentGain", algorithmBase.strategyBase.strategyOptions.minReentryPercentGain, new IterableOfDouble(0, 0.50, 0.01)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("SO maxReenterTimes", algorithmBase.strategyBase.strategyOptions.maxReenterTimesPerPosition, new IterableOfInteger(1, 5, 1)));

     	//Intervals
     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("SO entryAfterStopLossMinutes", algorithmBase.strategyBase.strategyOptions.intervalForEntryAfterExitWithLossMins, new IterableOfInteger(1, 20, 1)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("SO intervalForEntryWithSameSignalPointType", algorithmBase.strategyBase.strategyOptions.intervalForEntryWithSameSignalPointType, new IterableOfInteger(1, 20, 1)));
     	
     	//Misc
     	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("SO maxTransactionsPerDay", algorithmBase.strategyBase.strategyOptions.maxTransactionsDay, new IterableOfInteger(3, 32, 1)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO disableAfterYield", algorithmBase.strategyBase.strategyOptions.disableAfterYield, new IterableOfDouble(0, 3.00, 0.10)));
     	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble("SO disableAfterLoss", algorithmBase.strategyBase.strategyOptions.disableAfterLoss, new IterableOfDouble(-3.00, 0, 0.10)));
     	
		return listOfAdjustmentBase;
	}
}

