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
		none,
	}
	
	public enum StrategyActionCause {
		cease_external_condition_time,
		cease_algorithm_condition_trans,
		cease_algorithm_condition_profit,
		cease_algorithm_condition_stoploss,
		cease_algorithm_condition_nilchange,
		cease_algorithm_disabled,
		proceed_changed,
		none,
	}
}
