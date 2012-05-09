package com.autoStock.position;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.trading.types.TypePosition;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionCallback {
	// Will throw if connected to the exchange
	public static void setPositionSuccess(TypePosition typePosition) throws IllegalAccessError {
		if (typePosition.positionType == PositionType.position_long_entry){typePosition.positionType = PositionType.position_long;}
		else if (typePosition.positionType == PositionType.position_short_entry){typePosition.positionType = PositionType.position_short;}
		else if (typePosition.positionType == PositionType.position_long_exit){typePosition.positionType = PositionType.position_exited;}
		else if (typePosition.positionType == PositionType.position_short_exit){typePosition.positionType = PositionType.position_exited;}
		else throw new UnsupportedOperationException("No condition matched PositionType: " + typePosition.positionType.name());
	}
	
	public static void setPositionFailure(){
		
	}
}
