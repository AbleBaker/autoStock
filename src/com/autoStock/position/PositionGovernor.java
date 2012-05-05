package com.autoStock.position;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalType;
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
		
		if (signal.getCombinedSignal() > 25){
			governTrendUp(typeQuoteSlice, signal, positionGovernorResponse);
		} else if (signal.getCombinedSignal() < -25){
			governTrendDown(typeQuoteSlice, signal, positionGovernorResponse);
		} else {
			governTrendFlat(typeQuoteSlice, signal, positionGovernorResponse);
		}
		
		return positionGovernorResponse;
	}
	
	private void governTrendUp(TypeQuoteSlice typeQuoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse){
		signal.currentSignalType = SignalType.type_trend_up;
		
		positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_buy);
	}
	
	private void governTrendDown(TypeQuoteSlice typeQuoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse){
		signal.currentSignalType = SignalType.type_trend_down;
		
		if (positionManager.getPosition(typeQuoteSlice.symbol) != null){
			positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_sell);	
		}else{
			positionManager.suggestPosition(typeQuoteSlice, signal, PositionType.position_short);
		}
	}
	
	private void governTrendFlat(TypeQuoteSlice typeQuoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse){
		signal.currentSignalType = SignalType.type_trend_flat;
		positionManager.updatePositionPrice(typeQuoteSlice);
		
	}
}
