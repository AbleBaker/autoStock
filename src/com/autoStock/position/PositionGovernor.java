package com.autoStock.position;

import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions;
import com.autoStock.signal.SignalDefinitions.SignalPoint;
import com.autoStock.tools.Lock;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernor {
	private PositionManager positionManager = PositionManager.instance;
	private static boolean canGoLong = true;
	private static boolean canGoShort = false;
	private Lock lock = new Lock();
	
	public PositionGovernorResponse informGovener(QuoteSlice quoteSlice, Signal signal, Exchange exchange){
		synchronized (lock) {
			PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
			Position position = positionManager.getPosition(quoteSlice.symbol);
			
			signal.currentSignalType = SignalDefinitions.getSignalType(signal);
			positionManager.updatePositionPrice(quoteSlice, position);
			
			if (position == null){ //No position
				if (!AlgorithmCondition.canTradeOnDate(quoteSlice, exchange)){return positionGovernorResponse;}
				
				if (signal.getSignalPointMajority(false) == SignalPoint.long_entry && canGoLong){
					governLongEntry(quoteSlice, position, signal, positionGovernorResponse);
				}else if (signal.getSignalPointMajority(false) == SignalPoint.short_entry && canGoShort){
					governShortEntry(quoteSlice, position, signal, positionGovernorResponse);
				}
			} else { // Have position
	//			Co.println("--> Signal point majority: " + signal.getSignalPointMajority(true));
	
				if (position.positionType == PositionType.position_long || position.positionType == PositionType.position_long_entry) {
					if (signal.getSignalPointMajority(true) == SignalPoint.long_exit) {
						governLongExit(quoteSlice, position, signal, positionGovernorResponse);
					}
				}
	
				else if (position.positionType == PositionType.position_short || position.positionType == PositionType.position_short_entry) {
					if (signal.getSignalPointMajority(true) == SignalPoint.short_exit) {
						governShortExit(quoteSlice, position, signal, positionGovernorResponse);
					}
				}
			}
	
			return positionGovernorResponse;
		}
	}
	
	private void governLongEntry(QuoteSlice quoteSlice, Position typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_long || typePosition.positionType == PositionType.position_long_entry)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(quoteSlice, signal, PositionType.position_long_entry);
		positionGovernorResponse.changedPosition = true;
	}
	
	private void governShortEntry(QuoteSlice quoteSlice, Position typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_short || typePosition.positionType == PositionType.position_short_entry)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(quoteSlice, signal, PositionType.position_short_entry);
		positionGovernorResponse.changedPosition = true;
	}
	
	private void governLongExit(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_long_exit)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(quoteSlice, signal, PositionType.position_long_exit);
		positionGovernorResponse.changedPosition = true;
	}
	
	private void governShortExit(QuoteSlice quoteSlice, Position typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_short_exit)){
			return;
		}
		positionGovernorResponse.typePosition = positionManager.suggestPosition(quoteSlice, signal, PositionType.position_short_exit);
		positionGovernorResponse.changedPosition = true;
	}
}
