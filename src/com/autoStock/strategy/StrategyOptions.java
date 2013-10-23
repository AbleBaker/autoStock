package com.autoStock.strategy;

import java.util.ArrayList;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalPointTacticResolver.SignalPointTactic;
import com.autoStock.types.basic.MutableDouble;
import com.autoStock.types.basic.MutableInteger;
import com.rits.cloning.Cloner;

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
	public boolean mustHavePositiveSlice;
	public boolean disableAfterLoss; 
	public SignalPointTactic signalPointTacticForEntry;
	public SignalPointTactic signalPointTacticForExit;
	
	public int maxTransactionsDay;
	public MutableDouble minReentryPercentGain = new MutableDouble();
	public MutableDouble maxStopLossPercent = new MutableDouble();
	public MutableDouble maxProfitDrawdownPercent = new MutableDouble();
	public int maxNilChangePrice;
	public int maxNilChangeVolume;
	public int maxPositionEntryTime;
	public int maxPositionExitTime;
	public int maxPositionLossTime;
	public MutableInteger maxReenterTimesPerPosition = new MutableInteger();
	public MutableInteger intervalForReentryMins = new MutableInteger();
	public MutableInteger intervalForEntryAfterExitWithLossMins = new MutableInteger();
	public MutableInteger prefillShift = new MutableInteger();
	public MutableDouble disableAfterYield = new MutableDouble();
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
		string += "\n - Disable after yield: " + disableAfterYield.value;
		string += "\n - Max position entry time: " + maxPositionEntryTime;
		string += "\n - Max position exit time: " + maxPositionExitTime;
		string += "\n - Max position loss time: " + maxPositionLossTime;
		string += "\n - Max stop loss percent: " +  maxStopLossPercent.value;
		string += "\n - Max profit drawdown percent: " +  maxProfitDrawdownPercent.value;
		string += "\n - Max transactions per day: " + maxTransactionsDay;
		string += "\n - Signal point tactic (entry): " + signalPointTacticForEntry.name();
		string += "\n - Signal point tactic (exit): " + signalPointTacticForExit.name();
		string += "\n - Entry after loss interval minutes: " + intervalForEntryAfterExitWithLossMins.value;
		string += "\n - Reentry interval minutes: " + intervalForReentryMins.value;
		string += "\n - Reentry maximum frequency: " + maxReenterTimesPerPosition.value;
		string += "\n - Reentry minimum gain: " + minReentryPercentGain.value;

		return string;
	}

	public StrategyOptions copy() {
//		return new Gson().fromJson(new Gson().toJson(this), getClass());
		return new Cloner().deepClone(this);
	}
}
