package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions;
import com.autoStock.signal.SignalDefinitions.SignalPoint;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernor {
	public static PositionGovernor instance = new PositionGovernor();
	private PositionManager positionManager = PositionManager.instance;
	private static boolean canGoLong = true;
	private static boolean canGoShort = false;
	
	public synchronized PositionGovernorResponse informGovener(QuoteSlice quoteSlice, Signal signal, Exchange exchange){
		PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
		Position position = positionManager.getPosition(quoteSlice.symbol);
		
		signal.currentSignalTrend = SignalDefinitions.getSignalType(signal);
		positionManager.updatePositionPrice(quoteSlice, position);
		
		if (position == null){
			if (new AlgorithmCondition().canTradeOnDate(quoteSlice.dateTime, exchange) == false){
				return positionGovernorResponse;
			}
			
			Co.println("--> Position is null for : " + quoteSlice.symbol + ", " + signal.getSignalPointMajority(false, PositionType.position_none).name());
			
			if (signal.getSignalPointMajority(false, PositionType.position_none) == SignalPoint.long_entry && canGoLong){
				governLongEntry(quoteSlice, position, signal, positionGovernorResponse);
			}else if (signal.getSignalPointMajority(false, PositionType.position_none) == SignalPoint.short_entry && canGoShort){
				governShortEntry(quoteSlice, position, signal, positionGovernorResponse);
			}
		} else {
			boolean algorithmConditionExit = new AlgorithmCondition().shouldRequestExit(quoteSlice.dateTime, exchange, position);
			
			Co.println("--> Existing position! " + position.positionType.name());

			if (position.positionType == PositionType.position_long || position.positionType == PositionType.position_long_entry) {
				if ((signal.getSignalPointMajority(true, position.positionType) == SignalPoint.long_exit) || algorithmConditionExit) {
					governLongExit(quoteSlice, position, signal, positionGovernorResponse);
				}
			}

			else if (position.positionType == PositionType.position_short || position.positionType == PositionType.position_short_entry) {
				if ((signal.getSignalPointMajority(true, position.positionType) == SignalPoint.short_exit) || algorithmConditionExit) {
					governShortExit(quoteSlice, position, signal, positionGovernorResponse);
				}
			}else {
				throw new IllegalStateException();
			}
		}

		return positionGovernorResponse;
	}
	
	private void governLongEntry(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_long || position.positionType == PositionType.position_long_entry)){
			return;
		}
		positionGovernorResponse.position = positionManager.suggestPosition(quoteSlice, signal, PositionType.position_long_entry);
		if (positionGovernorResponse.position.units > 0){
			positionGovernorResponse.changedPosition = true;
		}
	}
	
	private void governShortEntry(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_short || position.positionType == PositionType.position_short_entry)){
			return;
		}
		positionGovernorResponse.position = positionManager.suggestPosition(quoteSlice, signal, PositionType.position_short_entry);
		if (positionGovernorResponse.position.units > 0){
			positionGovernorResponse.changedPosition = true;
		}
	}
	
	private void governLongExit(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_long_exit)){
			return;
		}
		positionGovernorResponse.position = positionManager.suggestPosition(quoteSlice, signal, PositionType.position_long_exit);
		positionGovernorResponse.changedPosition = true;
	}
	
	private void governShortExit(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_short_exit)){
			return;
		}
		positionGovernorResponse.position = positionManager.suggestPosition(quoteSlice, signal, PositionType.position_short_exit);
		positionGovernorResponse.changedPosition = true;
	}
}
