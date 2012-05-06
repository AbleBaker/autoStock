/**
 * 
 */
package com.autoStock.position;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.trading.types.TypePosition;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernorResponse {
	public boolean changedPosition = false;
	public PositionType positionType = PositionType.position_none;
	public TypePosition typePosition = null;
	
	public void setResponse(boolean changedPosition, TypePosition typePosition){
		this.changedPosition = changedPosition;
		this.typePosition =  typePosition;
		this.positionType = typePosition.positionType;
	}
	
	public void setResponse(boolean changedPosition, PositionType positionType){
		this.changedPosition = changedPosition;
		this.positionType = positionType;
	}
}
