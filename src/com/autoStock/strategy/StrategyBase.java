package com.autoStock.strategy;

import java.util.Date;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.position.PositionGovernor;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalPointMethod.SignalPointTactic;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 * 
 */
public class StrategyBase {
	public Signal signal;
	public SignalPointTactic signalPointTactic;
	public StrategyOptions strategyOptions;
	public AlgorithmCondition algorithmCondition;
	public AlgorithmBase algorithmBase;
	public final PositionGovernor positionGovener = PositionGovernor.getInstance();

	public StrategyBase(AlgorithmBase algorithmBase) {
		this.algorithmBase = algorithmBase;
	}

	public int getUpdatedPeriodLength(Date date, Exchange exchange, int periodLength) {
		if (strategyOptions.taperPeriodLength == false) {
			return periodLength;
		} else {
			if (algorithmCondition.taperPeriodLengthLower(date, exchange)) {
				if (periodLength > SignalControl.periodLengthStart) {
					periodLength--;
				}
			} else {
				if (periodLength < SignalControl.periodLengthEnd) {
					periodLength++;
				}
			}
		}
		
		return periodLength;
	}
}
