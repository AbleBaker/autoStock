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
	public Position position = new Position();
	
	public void setResponse(boolean changedPosition, Position position){
		this.changedPosition = changedPosition;
		this.position =  position;
	}
}
