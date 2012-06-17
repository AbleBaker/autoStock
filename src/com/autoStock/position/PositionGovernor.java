package com.autoStock.position;

import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalDefinitions;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernor {
	private PositionManager positionManager = PositionManager.instance;
	public static PositionGovernor instance = new PositionGovernor();
	
	public PositionGovernorResponse informGovener(QuoteSlice quoteSlice, Signal signal, Exchange exchange){
		PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
		Position typePosition = positionManager.getPosition(quoteSlice.symbol);
		
		signal.currentSignalType = SignalDefinitions.getSignalType(signal);
		positionManager.updatePositionPrice(quoteSlice, typePosition);
		
		if (typePosition == null){
			if (!AlgorithmCondition.canTradeOnDate(quoteSlice, exchange)){
				return positionGovernorResponse;
			}
			
			if (signal.getCombinedSignal() >= SignalControl.pointToSignalLongEntry){
				//Enter long
				governLongEntry(quoteSlice, null, signal, positionGovernorResponse);
				positionGovernorResponse.changedPosition = true;
			}
			else if (signal.getCombinedSignal() <= SignalControl.pointToSignalShortEntry){
				// Enter short
				governShortEntry(quoteSlice, null, signal, positionGovernorResponse);
				positionGovernorResponse.changedPosition = true;
			}
		}else{
			if (typePosition.positionType == PositionType.position_long_entry || typePosition.positionType == PositionType.position_long){
				if (signal.getCombinedSignal() <= SignalControl.pointToSignalLongExit){
					// Exit long
					governLongExit(quoteSlice, typePosition, signal, positionGovernorResponse);
					positionGovernorResponse.changedPosition = true;
				}
			}
			
			if (typePosition.positionType == PositionType.position_short_entry || typePosition.positionType == PositionType.position_short){
				if (signal.getCombinedSignal() >= SignalControl.pointToSignalShortExit){
					//Exit short
					governShortExit(quoteSlice, typePosition, signal, positionGovernorResponse);
					positionGovernorResponse.changedPosition = true;
				}
			}
		}
		
		return positionGovernorResponse;
	}
	
	private void governLongEntry(QuoteSlice typeQuoteSlice, Position typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_long || typePosition.positionType == PositionType.position_long_entry)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_long_entry);
	}
	
	private void governShortEntry(QuoteSlice typeQuoteSlice, Position typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_short || typePosition.positionType == PositionType.position_short_entry)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_short_entry);
	}
	
	private void governLongExit(QuoteSlice typeQuoteSlice, Position typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_long_exit)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_long_exit);
	}
	
	private void governShortExit(QuoteSlice typeQuoteSlice, Position typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_short_exit)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_short_exit);
	}
}
