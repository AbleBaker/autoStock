package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseReason;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseStatus;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPoint;
import com.autoStock.signal.SignalPointMethod;
import com.autoStock.signal.SignalPointMethod.SignalPointTactic;
import com.autoStock.strategy.StrategyOptions;
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
	
	public static PositionGovernor getInstance(){
		return instance;
	}
	
	public synchronized PositionGovernorResponse informGovener(QuoteSlice quoteSlice, Signal signal, Exchange exchange, StrategyOptions strategyOptions){
		return informGovener(quoteSlice, signal, exchange, strategyOptions, false);
	}
	
	public synchronized PositionGovernorResponse informGovener(QuoteSlice quoteSlice, Signal signal, Exchange exchange, StrategyOptions strategyOptions, boolean requestExit){
		PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
		Position position = positionManager.getPosition(quoteSlice.symbol);
		SignalPoint signalPoint = SignalPoint.none;
		
		if (position == null){
			signalPoint = SignalPointMethod.getSignalPoint(false, signal, PositionType.position_none, strategyOptions.signalPointTactic);
			
			if (signalPoint == SignalPoint.long_entry && strategyOptions.canGoLong){
				position = governLongEntry(quoteSlice, signal, positionGovernorResponse);
			}else if (signalPoint == SignalPoint.short_entry && strategyOptions.canGoShort){
				position = governShortEntry(quoteSlice, signal, positionGovernorResponse);
			}
		} else {
			signalPoint = SignalPointMethod.getSignalPoint(true, signal, position.positionType, strategyOptions.signalPointTactic);

			if (position.positionType == PositionType.position_long || position.positionType == PositionType.position_long_entry) {
				if (signalPoint == SignalPoint.long_exit || requestExit) {
					governLongExit(quoteSlice, position, signal, positionGovernorResponse);
				}
			}else if (position.positionType == PositionType.position_short || position.positionType == PositionType.position_short_entry) {
				if (signalPoint == SignalPoint.short_exit || requestExit) {
					governShortExit(quoteSlice, position, signal, positionGovernorResponse);
				}
			}else {
				throw new IllegalStateException();
			}
		}

		positionGovernorResponse.position = position;
//		if (signal.currentSignalPoint == signalPoint){
//			positionGovernorResponse.signalPoint = SignalPoint.no_change;
//			positionGovernorResponse.signalPoint.signalMetricType = SignalMetricType.no_change;
//		}else{
			positionGovernorResponse.signalPoint = signalPoint;
			signal.currentSignalPoint = signalPoint;
//		}

		return positionGovernorResponse;
	} 
	
	private Position governLongEntry(QuoteSlice quoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse){
		Position position = positionManager.executePosition(quoteSlice, signal, PositionType.position_long_entry);
		if (position == null){
			positionGovernorResponse.getFailedResponse(PositionGovernorResponseReason.failed_insufficient_funds);
		}else{
			positionGovernorResponse.status = PositionGovernorResponseStatus.changed_long_entry;
		}
		
		return position;
	}
	
	private Position governShortEntry(QuoteSlice quoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse){
		Position position = positionManager.executePosition(quoteSlice, signal, PositionType.position_short_entry);
		if (position == null){
			positionGovernorResponse.getFailedResponse(PositionGovernorResponseReason.failed_insufficient_funds);
		}else{
			positionGovernorResponse.status = PositionGovernorResponseStatus.changed_short_entry;
		}
		
		return position;
	}
	
	private void governLongExit(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_long_exit)){
			throw new IllegalStateException();
		}
		position = positionManager.executePosition(quoteSlice, signal, PositionType.position_long_exit);
		positionGovernorResponse.status = PositionGovernorResponseStatus.changed_long_exit;
	}
	
	private void governShortExit(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_short_exit)){
			throw new IllegalStateException();
		}
		position = positionManager.executePosition(quoteSlice, signal, PositionType.position_short_exit);
		positionGovernorResponse.status = PositionGovernorResponseStatus.changed_short_exit;
	}
}
