package com.autoStock.strategy;

import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.signal.Signal;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyResponse {
	public PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
	public StrategyAction strategyAction = StrategyAction.none;
	public StrategyActionCause strategyActionCause = StrategyActionCause.none;
	public QuoteSlice quoteSlice;
	public Signal signal;
	
	public enum StrategyAction {
		algorithm_disable,
		algorithm_pass,
		algorithm_proceed,
		algorithm_changed,
		no_change,
		none,
	}
	
	public enum StrategyActionCause {
		cease_condition_time_entry,
		cease_condition_time_exit,
		cease_condition_time_loss,
		cease_condition_trans,
		cease_condition_profit,
		cease_condition_stoploss,
		cease_condition_profit_drawdown,
		cease_condition_loss,
		cease_condition_nilchange,
		cease_condition_nilvolume,
		pass_condition_quotslice,
		pass_condition_previous_loss,
		cease_disabled,
		position_governor_failure,
		proceed_changed,
		none,
	}
}
