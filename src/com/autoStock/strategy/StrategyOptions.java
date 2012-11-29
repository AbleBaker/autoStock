package com.autoStock.strategy;

import com.autoStock.signal.SignalPointMethod.SignalPointTactic;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyOptions {
	public boolean canGoLong;
	public boolean canGoShort;
	public boolean canReenter;
	public boolean disableAfterNilChanges;
	public boolean disableAfterNilVolumes;
	public boolean taperPeriodLength;
	public boolean mustHavePositiveSlice;
	public boolean disableAfterLoss; 
	public SignalPointTactic signalPointTacticForEntry;
	public SignalPointTactic signalPointTacticForReentry;
	public SignalPointTactic signalPointTacticForExit;
	
	public int maxTransactionsDay = 4;
	public double minTakeProfitExit = 1.98d;
	public double minReentryPercentGain = 0.2d;
	public int maxStopLossValue = -35;
	public int maxNilChangePrice = 15;
	public int maxNilChangeVolume = 10;
	public int maxPositionEntryTime = 30;
	public int maxPositionTaperTime = 30;
	public int maxPositionExitTime = 5;
	public int maxReenterTimes = 3;
	public ImmutableInteger intervalForReentryMins = new ImmutableInteger();
}
