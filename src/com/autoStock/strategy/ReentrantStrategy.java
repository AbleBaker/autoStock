package com.autoStock.strategy;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalPointMethod;
import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class ReentrantStrategy {
	public static enum ReentryStatus {
		status_reenter,
		status_none,
	}
	
	public ReentryStatus getReentryStatus(Position position, Signal signal, StrategyOptions strategyOptions){
		
		return ReentryStatus.status_none;
	}
}
