package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalDefinitions;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPoint;
import com.autoStock.signal.SignalMetric;
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
	
	private static boolean canGoLong = true;
	private static boolean canGoShort = false;
	
	public synchronized PositionGovernorResponse informGovener(QuoteSlice quoteSlice, Signal signal, Exchange exchange){
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
	
//	@SuppressWarnings("unused")
//	public synchronized PositionGovernorResponse informGovener(QuoteSlice quoteSlice, Signal signal, Exchange exchange){
//		PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
//		Position typePosition = positionManager.getPosition(quoteSlice.symbol);
//		
//		signal.currentSignalType = SignalDefinitions.getSignalType(signal);
//		positionManager.updatePositionPrice(quoteSlice, typePosition);
//		
//		if (typePosition == null){
//			if (!AlgorithmCondition.canTradeOnDate(quoteSlice, exchange)){
//				return positionGovernorResponse;
//			}
//			
//			if ((signal.getCombinedSignal().strength >= signal.getCombinedSignal().longEntry) && canGoLong){
//				//Enter long
//				governLongEntry(quoteSlice, null, signal, positionGovernorResponse);
//				positionGovernorResponse.changedPosition = true;
//			}
//			else if ((signal.getCombinedSignal().strength <= signal.getCombinedSignal().shortEntry) && canGoShort){
//				// Enter short
//				governShortEntry(quoteSlice, null, signal, positionGovernorResponse);
//				positionGovernorResponse.changedPosition = true;
//			}
//		}else{
//			if (typePosition.positionType == PositionType.position_long_entry || typePosition.positionType == PositionType.position_long){
//				if (signal.getCombinedSignal().strength <= signal.getCombinedSignal().longExit){
//					// Exit long
//					governLongExit(quoteSlice, typePosition, signal, positionGovernorResponse);
//					positionGovernorResponse.changedPosition = true;
//				}
//			}
//			
//			if (typePosition.positionType == PositionType.position_short_entry || typePosition.positionType == PositionType.position_short){
//				if (signal.getCombinedSignal().strength >= signal.getCombinedSignal().shortExit){
//					//Exit short
//					governShortExit(quoteSlice, typePosition, signal, positionGovernorResponse);
//					positionGovernorResponse.changedPosition = true;
//				}
//			}
//		}
//		
//		return positionGovernorResponse;
//	}
	
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
	
	private void governLongExit(QuoteSlice quoteSlice, Position typePosition, Signal signal, PositionGovernorResponse positionGovernorResponse){
		if (typePosition != null && (typePosition.positionType == PositionType.position_long_exit)){
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
