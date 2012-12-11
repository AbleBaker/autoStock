package com.autoStock.strategy;

import com.autoStock.signal.SignalPointMethod.SignalPointTactic;
import com.autoStock.types.basic.ImmutableDouble;
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
	
	public int maxTransactionsDay;
	public double minTakeProfitExit;
	public ImmutableDouble minReentryPercentGain = new ImmutableDouble();
	public ImmutableInteger maxStopLossValue = new ImmutableInteger();
	public int maxNilChangePrice;
	public int maxNilChangeVolume;
	public int maxPositionEntryTime;
	public int maxPositionTaperTime;
	public int maxPositionExitTime;
	public ImmutableInteger maxReenterTimes = new ImmutableInteger();
	public ImmutableInteger intervalForReentryMins = new ImmutableInteger();
}
