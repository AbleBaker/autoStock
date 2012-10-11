package com.autoStock.strategy;

import java.util.ArrayList;

import javax.crypto.spec.PSource;

import com.autoStock.Co;
import com.autoStock.position.PositionGovernorResponse;
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
		double percentGainFromPosition = ((position.getPositionValue().valueCurrent / position.getPositionValue().valueFilled) -1) * 100;
		Time timeOfLastOccurrenceDifference = DateTools.getTimeUntilDate(positionGovernorResponseLast.dateOccurred, quoteSlice.dateTime);
		
		if (timeOfLastOccurrenceDifference.minutes > 5 || timeOfLastOccurrenceDifference.hours > 0){
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
		}
		
		return ReentryStatus.status_none;
	}
}
