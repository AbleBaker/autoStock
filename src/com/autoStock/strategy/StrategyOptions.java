package com.autoStock.strategy;

import com.autoStock.signal.SignalPointMethod.SignalPointTactic;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyOptions {
	public boolean canGoLong;
	public boolean canGoShort;
	public boolean disableAfterNilChanges;
	public boolean taperPeriodLength;
	public SignalPointTactic signalPointTactic;
	
	public int maxTransactionsDay = 4;
	public double minTakeProfitExit = 1.020d;
	public int maxStopLossValue = -35;
	public int maxNilChanges = 16;
	public int maxPositionEntryTime = 30;
	public int maxPositionTaperTime = 30;
	public int maxPositionExitTime = 8;
}
