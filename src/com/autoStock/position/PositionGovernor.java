package com.autoStock.position;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalDefinitions;
import com.autoStock.trading.types.TypePosition;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernor {
	private PositionManager positionManager = PositionManager.instance;
	public static PositionGovernor instance = new PositionGovernor();
	
	public PositionGovernorResponse informGovener(TypeQuoteSlice typeQuoteSlice, Signal signal){
		PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
		TypePosition typePosition = positionManager.getPosition(typeQuoteSlice.symbol);
		
		signal.currentSignalType = SignalDefinitions.getSignalType(signal);
		positionManager.updatePositionPrice(typeQuoteSlice, typePosition);
		
		if (typePosition == null){
			if (signal.getCombinedSignal() >= SignalControl.pointToSignalLongEntry){
				//Enter long
				governLongEntry(typeQuoteSlice, null, signal, positionGovernorResponse);
				positionGovernorResponse.changedPosition = true;
			}
			else if (signal.getCombinedSignal() <= SignalControl.pointToSignalShortEntry){
				// Enter short
				governShortEntry(typeQuoteSlice, null, signal, positionGovernorResponse);
				positionGovernorResponse.changedPosition = true;
			}
		}else{
			if (typePosition.positionType == PositionType.position_long_entry || typePosition.positionType == PositionType.position_long){
				if (signal.getCombinedSignal() <= SignalControl.pointToSignalLongExit){
					// Exit long
					governLongExit(typeQuoteSlice, typePosition, signal, positionGovernorResponse);
					positionGovernorResponse.changedPosition = true;
				}
			}
			
			if (typePosition.positionType == PositionType.position_short_entry || typePosition.positionType == PositionType.position_short){
				if (signal.getCombinedSignal() >= SignalControl.pointToSignalShortExit){
					//Exit short
					governShortExit(typeQuoteSlice, typePosition, signal, positionGovernorResponse);
					positionGovernorResponse.changedPosition = true;
				}
			}
		}
		
		return positionGovernorResponse;
	}
	
	private void governLongEntry(TypeQuoteSlice typeQuoteSlice, TypePosition typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_long || typePosition.positionType == PositionType.position_long_entry)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_long_entry);
	}
	
	private void governShortEntry(TypeQuoteSlice typeQuoteSlice, TypePosition typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_short || typePosition.positionType == PositionType.position_short_entry)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_short_entry);
	}
	
	private void governLongExit(TypeQuoteSlice typeQuoteSlice, TypePosition typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_long_exit)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_long_exit);
	}
	
	private void governShortExit(TypeQuoteSlice typeQuoteSlice, TypePosition typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_short_exit)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_short_exit);
	}
}
