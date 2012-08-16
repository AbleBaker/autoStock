/**
 * 
 */
package com.autoStock.position;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionDefinitions {
	
	public enum PositionReason {
		reason_signal_threshold,
		reason_algorith_condition,
		reason_time,
		reason_none,
	}
	
	public enum PositionType {
		position_long_entry,
		position_short_entry,
		position_long,
		position_short,
		position_long_exit,
		position_short_exit,
		
		position_exited,
		position_failed,
		position_none,
		;
	}
}
