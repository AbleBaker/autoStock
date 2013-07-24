package com.autoStock.strategy;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalPointMethod.SignalPointTactic;
import com.autoStock.types.basic.ImmutableDouble;
import com.autoStock.types.basic.ImmutableInteger;
import com.google.gson.Gson;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyOptions implements Cloneable {
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
	public ImmutableDouble minReentryPercentGain = new ImmutableDouble();
	public ImmutableDouble maxStopLossPercent = new ImmutableDouble();
	public ImmutableDouble maxProfitDrawdownPercent = new ImmutableDouble();
	public int maxNilChangePrice;
	public int maxNilChangeVolume;
	public int maxPositionEntryTime;
	public int maxPositionExitTime;
	public ImmutableInteger maxReenterTimes = new ImmutableInteger();
	public ImmutableInteger intervalForReentryMins = new ImmutableInteger();
	public ImmutableInteger prefillShift = new ImmutableInteger(0);
	public boolean prefillEnabled;
	
	public ArrayList<SignalMetricType> listOfSignalMetricType = new ArrayList<SignalMetricType>();
	
	@Override
	public String toString() {
		String string = new String();
		string += "\n - Can go long: " + canGoLong;
		string += "\n - Can go short: " + canGoShort;
		string += "\n - Can reenter: " + canReenter;
		string += "\n - Enable prefill: " + prefillEnabled;
		string += "\n - Disable after nil changes: " + disableAfterNilChanges;
		string += "\n - Disable after nil changes in price: " + maxNilChangePrice;
		string += "\n - Disable after nil changes in volume: " + maxNilChangeVolume;
		string += "\n - Disable after a loss: " + disableAfterLoss;
		string += "\n - Max position entry time: " + maxPositionEntryTime;
		string += "\n - Max position exit time: " + maxPositionExitTime;
		string += "\n - Max stop loss percent: " +  maxStopLossPercent.value;
		string += "\n - Max profit drawdown percent: " +  maxProfitDrawdownPercent.value;
		string += "\n - Max transactions per day: " + maxTransactionsDay;
		string += "\n - Signal point tactic (entry): " + signalPointTacticForEntry.name();
		string += "\n - Signal point tactic (reentry): " + signalPointTacticForReentry.name();
		string += "\n - Signal point tactic (exit): " + signalPointTacticForExit.name();
		string += "\n - Taper period length: " + taperPeriodLength;
		string += "\n - Reentry interval minutes: " + intervalForReentryMins.value;
		string += "\n - Reentry maximum frequency: " + maxReenterTimes.value;
		string += "\n - Reentry minimum gain: " + minReentryPercentGain.value;

		return string;
	}

	public StrategyOptions copy() {
		return new Gson().fromJson(new Gson().toJson(this), getClass());
	}
}
