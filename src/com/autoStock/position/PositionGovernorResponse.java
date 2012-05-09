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
	public TypePosition typePosition = new TypePosition();
	
	public void setResponse(boolean changedPosition, TypePosition typePosition){
		this.changedPosition = changedPosition;
		this.typePosition =  typePosition;
	}
}
