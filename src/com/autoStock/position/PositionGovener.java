package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovener {
	private PositionManager positionManager = PositionManager.instance;
	
	public void informGovener(TypeQuoteSlice typeQuoteSlice, Signal signal){
		//Co.println("PositionGovener received new signal: " + signal.getCombinedSignal());
		
		boolean changedPosition;
		
		if (signal.getCombinedSignal() > 25){
			signal.currentSignalType = SignalType.type_buy;
			changedPosition = positionManager.suggestPosition(typeQuoteSlice, signal);
//		} else if (signal.getCombinedSignal() < -20){
//			signal.currentSignalType = SignalType.type_short;
//			changedPosition = positionManager.suggestPosition(typeQuoteSlice, signal);	
		} else if (signal.getCombinedSignal() < -10){
			signal.currentSignalType = SignalType.type_sell;
			changedPosition = positionManager.suggestPosition(typeQuoteSlice, signal);
		} else {
			signal.currentSignalType = SignalType.type_none;
		}
	}
}
