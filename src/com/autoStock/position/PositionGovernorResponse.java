/**
 * 
 */
package com.autoStock.position;

import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernorResponse {
	public boolean changedPosition = false;
	public Position typePosition = new Position();
	
	public void setResponse(boolean changedPosition, Position typePosition){
		this.changedPosition = changedPosition;
		this.typePosition =  typePosition;
	}
}
