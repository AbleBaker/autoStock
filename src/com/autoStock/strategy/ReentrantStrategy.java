package com.autoStock.strategy;

import java.util.ArrayList;

import javax.crypto.spec.PSource;

import com.autoStock.Co;
import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseStatus;
import com.autoStock.position.PositionValue;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.SignalPointMethod;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;
import com.autoStock.types.basic.Time;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 *
 */
public class ReentrantStrategy {
	public static enum ReentryStatus {
		status_reenter,
		status_none,
	}
	
	public ReentryStatus getReentryStatus(Position position, Signal signal, StrategyOptions strategyOptions, SignalPoint signalPoint, Pair<Symbol, ArrayList<PositionGovernorResponse>> listOfPair, QuoteSlice quoteSlice){
		PositionValue positionValue = position.getPositionValue();
		PositionGovernorResponse positionGovernorResponseLast = listOfPair.second.get(listOfPair.second.size()-1);
		double percentGainFromPosition = position.getCurrentPercentGainLoss(true);
		Time timeOfLastOccurrenceDifference = DateTools.getTimeUntilDate(quoteSlice.dateTime, positionGovernorResponseLast.dateOccurred);
		int reenteredCount = 0;
		
		for (PositionGovernorResponse positionGovernorResponse : listOfPair.second){
			if (positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry || positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry){
				reenteredCount++;
			}
		}
		
//		Co.println("--> Time: " + timeOfLastOccurrenceDifference.hours + ", " + timeOfLastOccurrenceDifference.minutes);
		
		if ((timeOfLastOccurrenceDifference.minutes >= strategyOptions.intervalForReentryMins || timeOfLastOccurrenceDifference.hours > 0) && reenteredCount <= strategyOptions.maxReenterTimes){
			if (signalPoint.signalPointType == SignalPointType.long_entry && position.positionType == PositionType.position_long){
				if (percentGainFromPosition > 0.2){
					return ReentryStatus.status_reenter;
				}
			}else if (signalPoint.signalPointType == SignalPointType.short_entry && position.positionType == PositionType.position_short){
				return ReentryStatus.status_reenter;
			}else{
				
			}
		}
		
		return ReentryStatus.status_none;
	}
}
