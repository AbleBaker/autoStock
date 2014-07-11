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
		strategyOptions.canReenter.value = true;
		strategyOptions.mustHavePositiveSlice = false;
		strategyOptions.disableAfterNilChanges = true;
		strategyOptions.disableAfterNilVolumes = true;
		strategyOptions.disableAfterLoss = false;
		strategyOptions.prefillEnabled = false;
		strategyOptions.signalPointTacticForEntry.value = SignalPointTactic.tactic_any;
		strategyOptions.signalPointTacticForExit.value = SignalPointTactic.tactic_any;
		
		strategyOptions.maxTransactionsDay.value = 8;
		strategyOptions.maxStopLossPercent.value = -0.10d;
		strategyOptions.maxProfitDrawdownPercent.value = -0.20d;
		strategyOptions.maxNilChangePrice = 15;
		strategyOptions.maxNilChangeVolume = 15;
		strategyOptions.maxPositionEntryTime = 30;
		strategyOptions.maxPositionExitTime = 10;
		strategyOptions.maxPositionLossTime = 0;
		strategyOptions.maxReenterTimesPerPosition.value = 3;
		strategyOptions.intervalForReentryMins.value = 2;
		strategyOptions.minReentryPercentGain.value = 0.25;
		strategyOptions.prefillShift.value = 0;
		strategyOptions.intervalForEntryAfterExitWithLossMins.value = 10;
		strategyOptions.intervalForEntryWithSameSignalPointType.value = 10;
		strategyOptions.disableAfterYield.value = 10.00d;
		
		return strategyOptions;
	}
}
