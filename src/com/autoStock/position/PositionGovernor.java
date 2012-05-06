package com.autoStock.position;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalDefinitions.SignalType;
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
		
		if (signal.getCombinedSignal() > SignalControl.pointToSignalUp){
			governTrendUp(typeQuoteSlice, signal, positionGovernorResponse);
		} else if (signal.getCombinedSignal() < SignalControl.pointToSignalDown){
			governTrendDown(typeQuoteSlice, signal, positionGovernorResponse);
		} else {
			governTrendFlat(typeQuoteSlice, signal, positionGovernorResponse);
		}
		
		return positionGovernorResponse;
	}
	
	private void governTrendUp(TypeQuoteSlice typeQuoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse){
		signal.currentSignalType = SignalType.type_trend_up;
		TypePosition typePosition = null;
		
		if (positionManager.getPosition(typeQuoteSlice.symbol) == null){
			typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_buy);
			positionGovernorResponse.setResponse(true, typePosition);
		}else if (positionManager.getPosition(typeQuoteSlice.symbol).positionType == PositionType.position_short){
			typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_sell);
			positionGovernorResponse.setResponse(true, typePosition);
		}
	}
	
	private void governTrendDown(TypeQuoteSlice typeQuoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse){
		signal.currentSignalType = SignalType.type_trend_down;
		TypePosition typePosition = null;
		
		if (positionManager.getPosition(typeQuoteSlice.symbol) != null && positionManager.getPosition(typeQuoteSlice.symbol).positionType == PositionType.position_buy){
			typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_sell);
			positionGovernorResponse.setResponse(true, typePosition);
		}
		
		else if (positionManager.getPosition(typeQuoteSlice.symbol) == null && signal.getCombinedSignal() < SignalControl.pointToSignalShort){
			typePosition = positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_short);
			positionGovernorResponse.setResponse(true, typePosition);
		}else{
			//throw new IllegalStateException("Have position for symbol, with state: " + typeQuoteSlice.symbol + ", " + positionManager.getPosition(typeQuoteSlice.symbol).positionType.name());
		}
	}
	
	private void governTrendFlat(TypeQuoteSlice typeQuoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse){
		signal.currentSignalType = SignalType.type_trend_flat;
		positionManager.updatePositionPrice(typeQuoteSlice);
		
	}
}
