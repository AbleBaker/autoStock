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
		strategyOptions.taperPeriodLength = false;
		strategyOptions.signalPointTacticForEntry = SignalPointTactic.tatic_change;
		strategyOptions.signalPointTacticForReentry = SignalPointTactic.tatic_change;
		strategyOptions.signalPointTacticForExit = SignalPointTactic.tatic_change;

		strategyOptions.maxTransactionsDay = 8;
		strategyOptions.minTakeProfitExit = 1.98d;
		strategyOptions.maxStopLossValue.value = -50;
		strategyOptions.maxNilChangePrice = 15;
		strategyOptions.maxNilChangeVolume = 15;
		strategyOptions.maxPositionEntryTime = 30;
		strategyOptions.maxPositionExitTime = 10;
		strategyOptions.maxPositionTaperTime = 30;
		strategyOptions.maxReenterTimes.value = 15;
		strategyOptions.intervalForReentryMins.value = 1;
		strategyOptions.minReentryPercentGain.value = 0.10;
	}
	
	public StrategyOptions getDefaultStrategyOptions(){
		return strategyOptions;
	}
}
