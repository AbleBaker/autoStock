package com.autoStock.strategy;

import com.autoStock.position.PGResponse;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyResponse {
	public PGResponse positionGovernorResponse = new PGResponse();
	public StrategyAction strategyAction = StrategyAction.none;
	
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
		changed_long_entry,
		changed_long_exit,
		changed_short_entry,
		changed_short_exit,
	}
}
