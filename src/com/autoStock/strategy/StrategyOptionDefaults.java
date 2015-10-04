package com.autoStock.strategy;

import com.autoStock.signal.TacticResolver.SignalPointTactic;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyOptionDefaults {
	public static StrategyOptionDefaults instance = new StrategyOptionDefaults();
	
	public static StrategyOptionDefaults getInstance(){
		return instance;
	}	
	public StrategyOptions getDefaultStrategyOptions(){
		StrategyOptions strategyOptions = new StrategyOptions();
		strategyOptions.canGoLong.value = true;
		strategyOptions.canGoShort.value = true;
		strategyOptions.canReenter.value = false;
		strategyOptions.mustHavePositiveSlice = false;
		strategyOptions.disableAfterNilChanges = true;
		strategyOptions.disableAfterNilVolumes = true;
		strategyOptions.enablePrefill = false;
		strategyOptions.enablePremise = true;
		strategyOptions.enableContext = true;
		strategyOptions.signalPointTacticForEntry.value = SignalPointTactic.tactic_any;
		strategyOptions.signalPointTacticForExit.value = SignalPointTactic.tactic_any;
		
		strategyOptions.maxTransactionsDay.value = 16;
		strategyOptions.maxStopLossPercent.value = -0.10d;
		strategyOptions.maxProfitDrawdownPercent.value = -0.10d;
		strategyOptions.maxNilChangePrice = 15;
		strategyOptions.maxNilChangeVolume = 15;
		strategyOptions.maxPositionEntryTime = 30;
		strategyOptions.maxPositionExitTime = 5;
		strategyOptions.maxPositionTimeAtLoss.value = 45;
		strategyOptions.maxPositionTimeAtProfit.value = 45;
		strategyOptions.minPositionTimeAtProfitYield.value = 0; //.25d;
		strategyOptions.maxReenterTimesPerPosition.value = 3;
		strategyOptions.intervalForReentryMins.value = 2;
		strategyOptions.minReentryPercentGain.value = 0.15;
		strategyOptions.prefillShift.value = 0;
		strategyOptions.intervalForEntryAfterExitWithLossMins.value = 5;
		strategyOptions.intervalForEntryWithSameSignalPointType.value = 0;
		strategyOptions.disableAfterLoss.value = -0.25d;
		strategyOptions.disableAfterYield.value = 0.75d;
		
		return strategyOptions;
	}
}
