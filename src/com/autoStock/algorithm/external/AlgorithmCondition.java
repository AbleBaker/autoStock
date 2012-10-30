package com.autoStock.algorithm.external;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionValue;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmCondition {
	private StrategyOptions strategyOptions;
	
	public AlgorithmCondition(StrategyOptions strategyOptions){
		this.strategyOptions = strategyOptions;
	}
	
	public boolean canTadeAfterTransactions(int transactions){
		if (transactions >= strategyOptions.maxTransactionsDay){
			return false;
		}
		
		return true;
	}
	
	public boolean canEnterTradeOnDate(Date date, Exchange exchange){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), strategyOptions.maxPositionEntryTime);		
	
		if (date.getHours() > dateForLastExecution.getHours() || ( date.getHours() >= dateForLastExecution.getHours() && date.getMinutes() >= dateForLastExecution.getMinutes())){
			return false;
		}
		
		return true;
	}
	
	public boolean canEnterWithQuoteSlice(QuoteSlice quoteSlice, SignalPoint signalPoint){
		if (signalPoint.signalPointType == SignalPointType.long_entry){
			if (quoteSlice.priceClose >= quoteSlice.priceOpen){
				return true;
			}
		}else if (signalPoint.signalPointType == SignalPointType.short_entry){
			if (quoteSlice.priceClose <= quoteSlice.priceOpen){
				return true;
			}
		}else {
			return true;
		}
		
		return false;
	}
	
	public boolean taperPeriodLengthLower(Date date, Exchange exchange){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), strategyOptions.maxPositionTaperTime);		
		if (date.getHours() > dateForLastExecution.getHours() || (date.getHours() >= dateForLastExecution.getHours() && date.getMinutes() >= dateForLastExecution.getMinutes())){
			return true;
		}
		
		return false;
	}
	
	public boolean takeProfit(Position position, QuoteSlice quoteSlice){
		double percentGainFromPosition = 0;
		PositionValue positionValue = position.getPositionValue();
		
		if (position.isFilled()){
			if (position != null && (position.positionType == PositionType.position_long || position.positionType == PositionType.position_short)){
				if (position.getLastKnownUnitPrice() != 0){
					percentGainFromPosition = positionValue.valueIntrinsicWithFees / positionValue.valueCurrentWithFees;
				}
			}
			
			return percentGainFromPosition >= strategyOptions.minTakeProfitExit;
		}
		
		return false;
	}
	
	public boolean canTradeAfterLoss(ArrayList<StrategyResponse> listOfStrategyResponse){
		for (StrategyResponse strategyResponse : listOfStrategyResponse){
			if (strategyResponse.positionGovernorResponse != null && strategyResponse.positionGovernorResponse.position != null){
				if (strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true) < 0){
					return false;
				}
			}
		}
		
		return true;
	}
	
	//TODO: This is inaccurate
	public boolean stopLoss(Position position){
		if (position.isFilled()){
			double valueGainFromPosition = 0;
			PositionValue positionValue = position.getPositionValue();

			valueGainFromPosition =  positionValue.valueCurrentWithFees - positionValue.valueIntrinsicWithFees;
			
//			Co.println("--> Value gain: " + valueGainFromPosition + (valueGainFromPosition < strategyOptions.maxStopLossValue));
		
			return valueGainFromPosition < strategyOptions.maxStopLossValue;
		}
		
		return false;
	}
	
	public boolean requestExitOnDate(Date date, Exchange exchange){
		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), strategyOptions.maxPositionExitTime);
		if (date.getHours() > dateForLastExecution.getHours() || (date.getHours() >= dateForLastExecution.getHours() && date.getMinutes() >= dateForLastExecution.getMinutes())){
			return true;
		}
		
		return false;
	}

	public boolean disableAfterNilChanges(ArrayList<QuoteSlice> listOfQuoteSlice) {
		if (strategyOptions.disableAfterNilChanges == false){return false;}
		
		int countOfNilChanges = 0;
		double price = 0;
		
		for (QuoteSlice quoteSlice : listOfQuoteSlice){
			if (quoteSlice.priceClose == price){
				countOfNilChanges++;
			}else{
				countOfNilChanges = 0;
			}
			
			price = quoteSlice.priceClose;
		}
		
		return countOfNilChanges >= strategyOptions.maxNilChangePrice;
	}
	
	public boolean disableAfterNilVolume(ArrayList<QuoteSlice> listOfQuoteSlice){
		if (strategyOptions.disableAfterNilVolumes == false){return false;}
		
		int countOfNilChanges = 0;
		
		for (QuoteSlice quoteSlice : listOfQuoteSlice){
			if (quoteSlice.sizeVolume == 0){
				countOfNilChanges++;
			}else{
				countOfNilChanges = 0;
			}
		}
		
		return countOfNilChanges >= strategyOptions.maxNilChangeVolume;
	}
}
