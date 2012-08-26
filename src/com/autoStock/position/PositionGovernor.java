package com.autoStock.position;

import com.autoStock.position.PGResponse.PositionGovernorResponseReason;
import com.autoStock.position.PGResponse.PositionGovernorResponseStatus;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalPoint;
import com.autoStock.signal.SignalPointMethod;
import com.autoStock.signal.SignalPointMethod.SignalPointTactic;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernor {
	private static PositionGovernor instance = new PositionGovernor();
	private PositionManager positionManager = PositionManager.instance; 
	private static boolean canGoLong = true;
	private static boolean canGoShort = false;
	private SignalPointTactic signalPointTactic = SignalPointTactic.tatic_change;
	
	public static PositionGovernor getInstance(){
		return instance;
	}
	
	public synchronized PGResponse informGovener(QuoteSlice quoteSlice, Signal signal, Exchange exchange){
		PGResponse positionGovernorResponse = new PGResponse();
		Position position = positionManager.getPosition(quoteSlice.symbol);
		
		if (position == null){
			SignalPoint signalPoint = SignalPointMethod.getSignalPoint(false, signal, PositionType.position_none, signalPointTactic);
			
			if (signalPoint == SignalPoint.long_entry && canGoLong){
				governLongEntry(quoteSlice, signal, positionGovernorResponse);
			}else if (signalPoint == SignalPoint.short_entry && canGoShort){
				governShortEntry(quoteSlice, signal, positionGovernorResponse);
			}
			
			signal.currentSignalPoint = signalPoint;
			
		} else {
			boolean algorithmConditionExit = false;
			SignalPoint signalPoint = SignalPointMethod.getSignalPoint(true, signal, position.positionType, signalPointTactic);
			
			PGResponse tempPositionGovernorResponse = new PGResponse();

			if (position.positionType == PositionType.position_long || position.positionType == PositionType.position_long_entry) {
				if (signalPoint == SignalPoint.long_exit || algorithmConditionExit) {
					governLongExit(quoteSlice, position, signal, positionGovernorResponse);
				}
			}else if (position.positionType == PositionType.position_short || position.positionType == PositionType.position_short_entry) {
				if (signalPoint == SignalPoint.short_exit || algorithmConditionExit) {
					governShortExit(quoteSlice, position, signal, positionGovernorResponse);
				}
			}else {
				throw new IllegalStateException();
			}
			
			signal.currentSignalPoint = signalPoint;
			
			if (tempPositionGovernorResponse.status == PositionGovernorResponseStatus.failed){
				return tempPositionGovernorResponse;
			}
		}

		return positionGovernorResponse;
	} 
	
	private void governLongEntry(QuoteSlice quoteSlice, Signal signal, PGResponse positionGovernorResponse){
		Position position = positionManager.executePosition(quoteSlice, signal, PositionType.position_long_entry);
		if (position == null){
			positionGovernorResponse.getFailedResponse(PositionGovernorResponseReason.failed_insufficient_funds);
		}else{
			positionGovernorResponse.position = position;
			positionGovernorResponse.status = PositionGovernorResponseStatus.status_changed_long_entry;
		}
	}
	
	private void governShortEntry(QuoteSlice quoteSlice, Signal signal, PGResponse positionGovernorResponse){
		Position position = positionManager.executePosition(quoteSlice, signal, PositionType.position_short_entry);
		if (position == null){
			positionGovernorResponse.getFailedResponse(PositionGovernorResponseReason.failed_insufficient_funds);
		}else{
			positionGovernorResponse.position = position;
			positionGovernorResponse.status = PositionGovernorResponseStatus.status_changed_short_entry;
		}
	}
	
	private void governLongExit(QuoteSlice quoteSlice, Position position, Signal signal, PGResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_long_exit)){
			return;
		}
		positionGovernorResponse.position = positionManager.executePosition(quoteSlice, signal, PositionType.position_long_exit);
		positionGovernorResponse.status = PositionGovernorResponseStatus.status_changed_long_exit;
	}
	
	private void governShortExit(QuoteSlice quoteSlice, Position position, Signal signal, PGResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_short_exit)){
			return;
		}
		positionGovernorResponse.position = positionManager.executePosition(quoteSlice, signal, PositionType.position_short_exit);
		positionGovernorResponse.status = PositionGovernorResponseStatus.status_change_short_exit;
	}
}
