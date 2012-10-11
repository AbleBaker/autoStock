package com.autoStock.strategy;

import com.autoStock.Co;
import com.autoStock.position.PositionValue;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.SignalPointMethod;
import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class ReentrantStrategy {
	public static enum ReentryStatus {
		status_reenter,
		status_none,
	}
	
	public ReentryStatus getReentryStatus(Position position, Signal signal, StrategyOptions strategyOptions, SignalPoint signalPoint){
		PositionValue positionValue = position.getPositionValue();
		double percentGainFromPosition = ((position.getPositionValue().valueCurrent / position.getPositionValue().valueFilled) -1) * 100;
		
		if (signalPoint.signalPointType == SignalPointType.long_entry && position.positionType == PositionType.position_long){
			if (percentGainFromPosition > 0.2){
				return ReentryStatus.status_reenter;	
			}else{
				Co.println("--> autoDesk?: " + position.getPositionValue().valueFilled + ", " + position.getPositionValue().valueCurrent);
				Co.println("--> Percent gain was insufficient: " + percentGainFromPosition + ", " + position.getPositionProfitLossAfterComission());
			}
		}else if (signalPoint.signalPointType == SignalPointType.short_entry && position.positionType == PositionType.position_short){
			return ReentryStatus.status_reenter;
		}else{
			
		}
		
		return ReentryStatus.status_none;
	}
}
