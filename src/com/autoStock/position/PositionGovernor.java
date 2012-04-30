package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernor {
	private PositionManager positionManager = PositionManager.instance;
	
	public PositionGovernorResponse informGovener(TypeQuoteSlice typeQuoteSlice, Signal signal){
		PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
		
		//Co.println("PositionGovener received new signal: " + signal.getCombinedSignal());
		
		if (signal.getCombinedSignal() > 25){
			signal.currentSignalType = SignalType.type_trend_up;
			positionManager.suggestPosition(typeQuoteSlice, signal);
		} else if (signal.getCombinedSignal() < -10){
			signal.currentSignalType = SignalType.type_trend_down;
			positionManager.suggestPosition(typeQuoteSlice, signal);
		} else {
			signal.currentSignalType = SignalType.type_none;
		}
		
		return positionGovernorResponse;
	}
}
