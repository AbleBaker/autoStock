package com.autoStock.strategy;

import com.autoStock.signal.SignalPointMethod.SignalPointTactic;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyOptions {
	public boolean canGoLong;
	public boolean canGoShort;
	public boolean canReenter;
	public boolean disableAfterNilChanges;
	public boolean taperPeriodLength;
	public boolean disableAfterLoss = true; 
	public SignalPointTactic signalPointTactic;
	
	public int maxTransactionsDay = 4;
	public double minTakeProfitExit = 1.020d;
	public double minReentryPercentGain = 0.2d;
	public int maxStopLossValue = -35;
	public int maxNilChangePrice = 15;
	public int maxNilChangeVolume = 10;
	public int maxPositionEntryTime = 30;
	public int maxPositionTaperTime = 30;
	public int maxPositionExitTime = 5;
	public int maxReenterTimes = 3;
	public int intervalForReentryMins = 5;
}
