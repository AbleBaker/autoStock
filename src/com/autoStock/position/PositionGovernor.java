package com.autoStock.position;

import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorReason;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseStatus;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalPointMethod;
import com.autoStock.signal.SignalDefinitions.SignalPoint;
import com.autoStock.signal.SignalPointMethod.SignalPointTactic;
import com.autoStock.signal.SignalTools;
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
	private AlgorithmCondition algorithmCondition = new AlgorithmCondition(); 
	private static boolean canGoLong = true;
	private static boolean canGoShort = false;
	private SignalPointTactic signalPointTactic = SignalPointTactic.tatic_change;
	
	public synchronized PositionGovernorResponse informGovener(QuoteSlice quoteSlice, Signal signal, Exchange exchange, int transactions, PositionGovernorResponse positionGovernorResponsePrevious){
		PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
		Position position = positionManager.getPosition(quoteSlice.symbol);
		
		signal.currentSignalTrend = SignalTools.getSignalType(signal);
		positionManager.updatePositionPrice(quoteSlice, position);
		
		if (position == null){
			SignalPoint signalPoint = SignalPointMethod.getSignalPoint(false, signal, PositionType.position_none, signalPointTactic);

			if (algorithmCondition.canTradeOnDate(quoteSlice.dateTime, exchange) == false){
				return positionGovernorResponse.getFailedResponse(PositionGovernorReason.failed_algorithm_condition_time);
			}

//			if (algorithmCondition.canTadeAfterTransactions(transactions) == false){
//				return positionGovernorResponse.getFailedResponse(PositionGovernorReason.failed_algorithm_condition_trans);
//			}
			
			if (signalPoint == SignalPoint.long_entry && canGoLong){
				governLongEntry(quoteSlice, signal, positionGovernorResponse);
			}else if (signalPoint == SignalPoint.short_entry && canGoShort){
				governShortEntry(quoteSlice, signal, positionGovernorResponse);
			}
			
			signal.currentSignalPoint = signalPoint;
			
		} else {
			boolean algorithmConditionExit = false;
			SignalPoint signalPoint = SignalPointMethod.getSignalPoint(true, signal, position.positionType, signalPointTactic);
			
			if (algorithmCondition.shouldRequestExitOnDate(quoteSlice.dateTime, exchange, position)){
				positionGovernorResponse.status.reason = PositionGovernorReason.failed_algorithm_condition_time;
				algorithmConditionExit = true;
			}
			
//			if (algorithmCondition.shouldStopLoss(position)){
//				positionGovernorResponse.status.reason = PositionGovernorReason.failed_algorithm_condition_stoploss;
//				algorithmConditionExit = true;
//			}

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
		}

		return positionGovernorResponse;
	} 
	
	private void governLongEntry(QuoteSlice quoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse){
		Position position = positionManager.executePosition(quoteSlice, signal, PositionType.position_long_entry);
		if (position == null){
			positionGovernorResponse.getFailedResponse(PositionGovernorReason.failed_insufficient_funds);
		}else{
			positionGovernorResponse.position = position;
			positionGovernorResponse.status = PositionGovernorResponseStatus.status_changed_long_entry;
		}
	}
	
	private void governShortEntry(QuoteSlice quoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse){
		Position position = positionManager.executePosition(quoteSlice, signal, PositionType.position_short_entry);
		if (position == null){
			positionGovernorResponse.getFailedResponse(PositionGovernorReason.failed_insufficient_funds);
		}else{
			positionGovernorResponse.position = position;
			positionGovernorResponse.status = PositionGovernorResponseStatus.status_changed_short_entry;
		}
	}
	
	private void governLongExit(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_long_exit)){
			return;
		}
		positionGovernorResponse.position = positionManager.executePosition(quoteSlice, signal, PositionType.position_long_exit);
		positionGovernorResponse.status = PositionGovernorResponseStatus.status_changed_long_exit;
	}
	
	private void governShortExit(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (position != null && (position.positionType == PositionType.position_short_exit)){
			return;
		}
		positionGovernorResponse.position = positionManager.executePosition(quoteSlice, signal, PositionType.position_short_exit);
		positionGovernorResponse.status = PositionGovernorResponseStatus.status_change_short_exit;
	}
}
