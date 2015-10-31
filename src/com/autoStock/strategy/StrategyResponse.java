package com.autoStock.strategy;

import com.autoStock.account.BasicAccount;
import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.signal.Signaler;
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
	public Signaler signal;
	public BasicAccount basicAccountCopy;
	
	public enum StrategyAction {
		algorithm_disable,
		algorithm_pass,
		algorithm_proceed,
		algorithm_changed,
		no_change,
		none,
	}
	
	public enum StrategyActionCause {
		disable_condition_time_entry,
		disable_condition_profit_loss,
		disable_condition_profit_yield,
		cease_condition_time_exit,
		cease_condition_time_profit,
		cease_condition_time_loss,
		cease_condition_trans,
		cease_condition_profit,
		cease_condition_stoploss,
		cease_condition_profit_drawdown,
		cease_condition_loss,
		cease_end_of_feed,
		disable_condition_nilchange,
		disable_condition_nilvolume,
		cease_disabled,
		pass_condition_quotslice,
		pass_condition_previous_loss,
		pass_condition_previous_exit_long,
		pass_condition_previous_exit_short,
		position_governor_failure,
		proceed_changed,
		none,
	}
}
