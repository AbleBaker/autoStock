package com.autoStock.strategy;

import com.autoStock.position.PositionGovernorResponse;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyResponse {
	public PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
	public StrategyAction strategyAction = StrategyAction.none;
	public StrategyActionCause strategyActionCause = StrategyActionCause.none;
	
	public enum StrategyAction {
		algorithm_disable,
		algorithm_proceed,
		algorithm_changed,
		no_change,
		none,
	}
	
	public enum StrategyActionCause {
		cease_condition_time,
		cease_condition_trans,
		cease_condition_profit,
		cease_condition_stoploss,
		cease_condition_nilchange,
		cease_disabled,
		proceed_changed,
		none,
	}
}
