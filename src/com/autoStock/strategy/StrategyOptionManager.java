package com.autoStock.strategy;

import com.autoStock.signal.SignalPointMethod.SignalPointTactic;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyOptionManager {
	public static StrategyOptionManager instance = new StrategyOptionManager();
	private StrategyOptions strategyOptions = new StrategyOptions();
	
	public static StrategyOptionManager getInstance(){
		return instance;
	}
	
	public StrategyOptionManager(){
		strategyOptions.canGoLong = true;
		strategyOptions.canGoShort = false;
		strategyOptions.canReenter = true;
		strategyOptions.mustHavePositiveSlice = true;
		strategyOptions.disableAfterNilChanges = true;
		strategyOptions.disableAfterNilVolumes = true;
		strategyOptions.disableAfterLoss = false;
		strategyOptions.taperPeriodLength = true;
		strategyOptions.signalPointTacticForEntry = SignalPointTactic.tatic_combined;
		strategyOptions.signalPointTacticForReentry = SignalPointTactic.tatic_combined;
		strategyOptions.signalPointTacticForExit = SignalPointTactic.tatic_combined;

		strategyOptions.maxTransactionsDay = 8;
		strategyOptions.minTakeProfitExit = 1.98d;
		strategyOptions.maxStopLossValue.value = -50;
		strategyOptions.maxNilChangePrice = 15;
		strategyOptions.maxNilChangeVolume = 15;
		strategyOptions.maxPositionEntryTime = 30;
		strategyOptions.maxPositionExitTime = 10;
		strategyOptions.maxPositionTaperTime = 30;
		strategyOptions.maxReenterTimes.value = 3;
		strategyOptions.intervalForReentryMins.value = 10;
		strategyOptions.minReentryPercentGain.value = 0.20;
	}
	
	public StrategyOptions getDefaultStrategyOptions(){
		return strategyOptions;
	}
}
