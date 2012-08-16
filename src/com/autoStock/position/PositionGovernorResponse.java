/**
 * 
 */
package com.autoStock.position;

import com.autoStock.position.PositionGovernorResponse.PositionGovernorReason;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseStatus;
import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernorResponse {
	public Position position;
	public PositionGovernorResponseStatus status = PositionGovernorResponseStatus.none;
	
	public enum PositionGovernorReason{
		failed_insufficient_funds,
		failed_algorithm_condition_time,
		failed_algorithm_condition_trans,
		failed_algorithm_condition_profit,
		failed_algorithm_condition_stoploss,
		none
	}
	
	public enum PositionGovernorResponseStatus {
		status_changed_long_entry,
		status_changed_short_entry,
		status_changed_long_exit,
		status_change_short_exit,
		
		failed,
		no_change,
		none,
		
		;
		
		public PositionGovernorReason reason = PositionGovernorReason.none;
		
		private PositionGovernorResponseStatus(PositionGovernorReason reason){
			this.reason = reason;
		}
		
		private PositionGovernorResponseStatus(){
			
		}
	}
	
	public PositionGovernorResponse getFailedResponse(PositionGovernorReason reason){
		this.status = PositionGovernorResponseStatus.failed;
		this.status.reason = reason;
		return this;
	}
}
