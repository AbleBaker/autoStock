package com.autoStock.strategy;

import com.autoStock.signal.SignalPointMethod.SignalPointTactic;

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
		strategyOptions.canGoLong = true;
		strategyOptions.canGoShort = true;
		strategyOptions.canReenter = true;
		strategyOptions.mustHavePositiveSlice = false;
		strategyOptions.disableAfterNilChanges = true;
		strategyOptions.disableAfterNilVolumes = true;
		strategyOptions.disableAfterLoss = false;
		strategyOptions.prefillEnabled = false;
		strategyOptions.signalPointTacticForEntry = SignalPointTactic.tatic_combined;
		strategyOptions.signalPointTacticForReentry = SignalPointTactic.tatic_change;
		strategyOptions.signalPointTacticForExit = SignalPointTactic.tatic_combined;
		
		strategyOptions.maxTransactionsDay = 16;
		strategyOptions.maxStopLossPercent.value = -0.45d;
		strategyOptions.maxProfitDrawdownPercent.value = -1.20d;
		strategyOptions.maxNilChangePrice = 15;
		strategyOptions.maxNilChangeVolume = 15;
		strategyOptions.maxPositionEntryTime = 30;
		strategyOptions.maxPositionExitTime = 10;
		strategyOptions.maxPositionLossTime = 30;
		strategyOptions.maxReenterTimes.value = 3;
		strategyOptions.intervalForReentryMins.value = 2;
		strategyOptions.minReentryPercentGain.value = 0.05;
		strategyOptions.prefillShift.value = 0;
		strategyOptions.intervalForEntryAfterExitWithLossMins.value = 10;
//		strategyOptions.intervalForExitEntryMins.value = 5;
		
		return strategyOptions;
	}
}
