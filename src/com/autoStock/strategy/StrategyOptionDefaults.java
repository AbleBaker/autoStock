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
		strategyOptions.canGoShort = false;
		strategyOptions.canReenter = true;
		strategyOptions.mustHavePositiveSlice = true;
		strategyOptions.disableAfterNilChanges = true;
		strategyOptions.disableAfterNilVolumes = true;
		strategyOptions.disableAfterLoss = false;
		strategyOptions.taperPeriodLength = false;
		strategyOptions.signalPointTacticForEntry = SignalPointTactic.tatic_combined;
		strategyOptions.signalPointTacticForReentry = SignalPointTactic.tatic_change;
		strategyOptions.signalPointTacticForExit = SignalPointTactic.tatic_change;
		
		strategyOptions.maxTransactionsDay = 32;
		strategyOptions.maxStopLossPercent.value = -0.25d;
		strategyOptions.maxNilChangePrice = 15;
		strategyOptions.maxNilChangeVolume = 15;
		strategyOptions.maxPositionEntryTime = 30;
		strategyOptions.maxPositionExitTime = 10;
		strategyOptions.maxPositionTaperTime = 30;
		strategyOptions.maxReenterTimes.value = 3;
		strategyOptions.intervalForReentryMins.value = 10;
		strategyOptions.minReentryPercentGain.value = 0.20;
		
		return strategyOptions;
	}
}
