package com.autoStock.strategy;

import java.util.Date;

import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.signal.SignalControl;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyHelper {
	public static synchronized int getUpdatedPeriodLength(Date date, Exchange exchange, int periodLength, StrategyOptions strategyOptions) {
		if (strategyOptions.taperPeriodLength == false) {
			return periodLength;
		} else {
			if (new AlgorithmCondition(strategyOptions).taperPeriodLengthLower(date, exchange)) {
//				if (periodLength > SignalControl.periodLengthEnd.value) {
//					periodLength--;
//				}
			} else {
				if (periodLength + 1 <= SignalControl.periodLengthMiddle.value) {
					periodLength++;
				}
			}
		}
		
		return periodLength;
	}
}
