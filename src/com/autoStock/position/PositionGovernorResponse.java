/**
 * 
 */
package com.autoStock.position;

import com.autoStock.position.PositionDefinitions.PositionType;

/**
 * @author kevink
 *
 */
public class PositionGovernorResponse {
	public boolean changedPosition = true;
	public PositionType positionType = PositionType.position_none;
}
