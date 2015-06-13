package com.autoStock.position;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.account.BasicAccount;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.TacticResolver;
import com.autoStock.strategy.ReentrantStrategy;
import com.autoStock.strategy.ReentrantStrategy.ReentryStatus;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionGovernor {
	private PositionManager positionManager;
	private ArrayList<Pair<Symbol,ArrayList<PositionGovernorResponse>>> listOfPairedResponses = new ArrayList<Pair<Symbol,ArrayList<PositionGovernorResponse>>>();
	private ReentrantStrategy reentrantStrategy = new ReentrantStrategy();
	private PositionGenerator positionGenerator = new PositionGenerator();
	
	public PositionGovernor(PositionManager positionManager){
		this.positionManager = positionManager;
	}
	
	public PositionGovernorResponse informGovener(QuoteSlice quoteSlice, Signal signal, Exchange exchange, StrategyOptions strategyOptions, boolean requestExit, Position position, PositionOptions positionOptions, BasicAccount basicAccount){
		PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
		SignalPoint signalPoint = new SignalPoint();
		
		if (position == null){
			signalPoint = TacticResolver.getSignalPoint(false, signal, PositionType.position_none, strategyOptions.signalPointTacticForEntry.value);
				
			if (signalPoint.signalPointType == SignalPointType.long_entry && strategyOptions.canGoLong.value){
				position = governLongEntry(quoteSlice, signal, positionGovernorResponse, exchange, positionOptions, basicAccount);
			}else if (signalPoint.signalPointType == SignalPointType.short_entry && strategyOptions.canGoShort.value){
				position = governShortEntry(quoteSlice, signal, positionGovernorResponse, exchange, positionOptions, basicAccount);
			}
		} else {
			SignalPoint signalPointForReentry = null; //SignalPointResolver.getSignalPoint(false, signal, PositionType.position_none, strategyOptions.signalPointTacticForReentry);
			signalPoint = TacticResolver.getSignalPoint(true, signal, position.positionType, strategyOptions.signalPointTacticForExit.value);

			if (position.positionType == PositionType.position_long || position.positionType == PositionType.position_long_entry) {
				if (signalPoint.signalPointType == SignalPointType.long_exit || requestExit) {
					governLongExit(quoteSlice, position, signal, positionGovernorResponse, exchange);
				}else if (strategyOptions.canReenter.value){
					//signalPoint.signalPointType == SignalPointType.reentry && 
					if (reentrantStrategy.getReentryStatus(position, signal, strategyOptions, signalPointForReentry, getPair(quoteSlice.symbol), quoteSlice) == ReentryStatus.status_reenter){
						governLongReentry(quoteSlice, position, signal, positionGovernorResponse, exchange, basicAccount);
					}
				}
			}else if (position.positionType == PositionType.position_short || position.positionType == PositionType.position_short_entry) {
				if (signalPoint.signalPointType == SignalPointType.short_exit || requestExit) {
					governShortExit(quoteSlice, position, signal, positionGovernorResponse, exchange);
				}else if (strategyOptions.canReenter.value){
					//signalPoint.signalPointType == SignalPointType.reentry && 
					if (reentrantStrategy.getReentryStatus(position, signal, strategyOptions, signalPointForReentry, getPair(quoteSlice.symbol), quoteSlice) == ReentryStatus.status_reenter){
						governShortReentry(quoteSlice, position, signal, positionGovernorResponse, exchange, basicAccount);
					}
				}
			}else if (position.positionType == PositionType.position_cancelled || position.positionType == PositionType.position_cancelling || position.positionType == PositionType.position_long_exited || position.positionType == PositionType.position_short_exited || position.positionType == PositionType.position_long_exit || position.positionType == PositionType.position_short_exit){
				Co.println("--> Position is not yet removed: " + position.symbol.symbolName);
			}else {
				throw new IllegalStateException("Position type did not match: " + position.positionType.name() + ", " + positionManager.getPositionListSize());
			}
		}

		positionGovernorResponse.position = position;
		if (position != null){positionGovernorResponse.positionValue = position.getPositionValue();}
		positionGovernorResponse.signalPoint = signalPoint;
		positionGovernorResponse.dateOccurred = quoteSlice.dateTime;
		signal.currentSignalPoint = signalPoint;
		
		if (getPair(quoteSlice.symbol) == null){
			synchronized (listOfPairedResponses) {
				listOfPairedResponses.add(new Pair<Symbol,ArrayList<PositionGovernorResponse>>(quoteSlice.symbol, new ArrayList<PositionGovernorResponse>()));				
			}
		}
		
		if (positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry
			|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry
			|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit
			|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry
			|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry
			|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
				getPair(quoteSlice.symbol).second.add(positionGovernorResponse);	
		}

		return positionGovernorResponse;
	} 
	
	private Position governLongEntry(QuoteSlice quoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse, Exchange exchange, PositionOptions positionOptions, BasicAccount basicAccount){
		Position position = positionManager.executePosition(quoteSlice, exchange, signal, PositionType.position_long_entry, null, positionOptions, basicAccount);
		if (position == null){
			positionGovernorResponse.getFailedResponse(PositionGovernorResponseReason.failed_insufficient_funds);
		}else{
			positionGovernorResponse.status = PositionGovernorResponseStatus.changed_long_entry;
		}
		
		return position;
	}
	
	private void governLongReentry(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse, Exchange exchange, BasicAccount basicAccount){
		int reentryUnits = positionGenerator.getPositionReentryUnits(quoteSlice.priceClose, signal, position.basicAccount);
		if (reentryUnits > 0){
			position.executeReentry(reentryUnits, quoteSlice.priceClose);
			positionGovernorResponse.status = PositionGovernorResponseStatus.changed_long_reentry;
		}else{
			positionGovernorResponse.getFailedResponse(PositionGovernorResponseReason.failed_insufficient_funds);
		}
	}
	
	private Position governShortEntry(QuoteSlice quoteSlice, Signal signal, PositionGovernorResponse positionGovernorResponse, Exchange exchange, PositionOptions positionOptions, BasicAccount basicAccount){
		Position position = positionManager.executePosition(quoteSlice, exchange, signal, PositionType.position_short_entry, null, positionOptions, basicAccount);
		if (position == null){
			positionGovernorResponse.getFailedResponse(PositionGovernorResponseReason.failed_insufficient_funds);
		}else{
			positionGovernorResponse.status = PositionGovernorResponseStatus.changed_short_entry;
		}
		
		return position;
	}

	private void governShortReentry(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse, Exchange exchange, BasicAccount basicAccount){
		int reentryUnits = positionGenerator.getPositionReentryUnits(quoteSlice.priceClose, signal, position.basicAccount);
		if (reentryUnits > 0){
			position.executeReentry(reentryUnits, quoteSlice.priceClose);
			positionGovernorResponse.status = PositionGovernorResponseStatus.changed_short_reentry;
		}else{
			positionGovernorResponse.getFailedResponse(PositionGovernorResponseReason.failed_insufficient_funds);
		}
	}
	
	private void governLongExit(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse, Exchange exchange){
		if (position != null && (position.positionType == PositionType.position_long_exit)){
			throw new IllegalStateException();
		}
		position = positionManager.executePosition(quoteSlice, exchange, signal, PositionType.position_long_exit, position, null, position.basicAccount);
		positionGovernorResponse.status = PositionGovernorResponseStatus.changed_long_exit;
	}
	
	private void governShortExit(QuoteSlice quoteSlice, Position position, Signal signal, PositionGovernorResponse positionGovernorResponse, Exchange exchange){
		if (position != null && (position.positionType == PositionType.position_short_exit)){
			throw new IllegalStateException();
		}
		position = positionManager.executePosition(quoteSlice, exchange, signal, PositionType.position_short_exit, position, null, position.basicAccount);
		positionGovernorResponse.status = PositionGovernorResponseStatus.changed_short_exit;
	}
	
	public PositionManager getPositionManager(){
		return positionManager;
	}
	
	private Pair<Symbol,ArrayList<PositionGovernorResponse>> getPair(Symbol symbol){
		synchronized (listOfPairedResponses){
			for (Pair<Symbol,ArrayList<PositionGovernorResponse>> pair : listOfPairedResponses){
				if (pair.first.symbolName.equals(symbol.symbolName)){
					return pair;
				}
			}
			return null;
		}
	}
	
	public void reset(){
		listOfPairedResponses.clear();			
	}
}
