/**
 * 
 */
package com.autoStock.position;

import com.autoStock.position.PositionDefinitions.PositionType;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernorResponse {
	public boolean changedPosition = true;
	public PositionType positionType = PositionType.position_none;
}
