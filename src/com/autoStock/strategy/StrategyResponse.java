package com.autoStock.strategy;

import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.signal.Signal;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyResponse implements Cloneable {
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
		cease_condition_trans,
		cease_condition_profit,
		cease_condition_stoploss,
		cease_condition_loss,
		cease_condition_nilchange,
		cease_condition_nilvolume,
		pass_condition_quotslice,
		cease_disabled,
		position_governor_failure,
		proceed_changed,
		none,
	}
	
	@Override
	public StrategyResponse clone(){
		try {
			return (StrategyResponse) super.clone();
		}catch(Exception e){
			return null;
		}
	}
}
