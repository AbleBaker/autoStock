/**
 * 
 */
package com.autoStock.position;

import com.autoStock.signal.SignalDefinitions.SignalPoint;
import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernorResponse {
	public Position position;
	public PositionGovernorResponseStatus status = PositionGovernorResponseStatus.none;
	public PositionGovernorResponseReason reason = PositionGovernorResponseReason.none;
	public SignalPoint signalPoint = SignalPoint.none;
	
	public enum PositionGovernorResponseReason{
		failed_insufficient_funds,
		algorithm_condition_time,
		algorithm_condition_trans,
		algorithm_condition_profit,
		algorithm_condition_stoploss,
		algorithm_is_disabled,
		none
	}
	
	public enum PositionGovernorResponseStatus {
		status_changed_long_entry,
		status_changed_short_entry,
		status_changed_long_exit,
		status_changed_short_exit,
		
		failed,
		none,
		;
	}
	
	public PositionGovernorResponse getFailedResponse(PositionGovernorResponseReason reason){
		status = PositionGovernorResponseStatus.failed;
		this.reason = reason;
		return this;
	}
}
