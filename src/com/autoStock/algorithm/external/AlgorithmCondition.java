package com.autoStock.algorithm.external;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.basic.Time;

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
	
//	public boolean takeProfit(Position position, QuoteSlice quoteSlice){
//		double percentGainFromPosition = 0;
//		PositionValue positionValue = position.getPositionValue();
//		
//		if (position.isFilled()){
//			if (position != null && (position.positionType == PositionType.position_long || position.positionType == PositionType.position_short)){
//				if (position.getLastKnownUnitPrice() != 0){
//					percentGainFromPosition = positionValue.valueIntrinsicWithFees / positionValue.valueCurrentWithFees;
//				}
//			}
//			
//			return percentGainFromPosition >= strategyOptions.minTakeProfitExit;
//		}
//		
//		return false;
//	}
	
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
	
	public boolean canTradeAfterLossInterval(Date date, ArrayList<StrategyResponse> listOfStrategyResponse){
		if (strategyOptions.intervalForEntryAfterExitWithLossMins.value == 0){
			return true;
		}
		
		for (StrategyResponse strategyResponse : listOfStrategyResponse){
			if (strategyResponse.positionGovernorResponse != null){
				if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
					
					if (strategyResponse.positionGovernorResponse.position.getPositionProfitLossBeforeComission() < 0){
						if ((date.getTime() - strategyResponse.positionGovernorResponse.dateOccurred.getTime()) / 60 /1000 < strategyOptions.intervalForEntryAfterExitWithLossMins.value){
							return false;
						}
					}					
//					Co.println("--> Minutes since exit: " + ( (date.getTime() - strategyResponse.positionGovernorResponse.dateOccurred.getTime()) / 60 /1000));
				}
				
			}
		}
		
		return true;
	}
	
	public boolean stopLoss(Position position){
		if (position.isFilledAndOpen()){			
			return position.getCurrentPercentGainLoss(false) < strategyOptions.maxStopLossPercent.value;
		}
		
		return false;
	}
	
	public boolean requestExitOnDate(Date date, Exchange exchange){
//		Date dateForLastExecution = DateTools.getChangedDate(DateTools.getDateFromTime(exchange.timeCloseForeign), strategyOptions.maxPositionExitTime);
		
		Time time = DateTools.getTimeFromDate(date);
		Time timeForLastExecution = new Time(exchange.timeCloseForeign.hours, exchange.timeCloseForeign.minutes, exchange.timeCloseForeign.seconds);
		
		if (exchange.timeCloseForeign.minutes == 0){
			timeForLastExecution.hours--;
			timeForLastExecution.minutes = 60 - strategyOptions.maxPositionExitTime;
		}else{
			timeForLastExecution.minutes -= strategyOptions.maxPositionExitTime;
		}
		
		
		if (time.hours > timeForLastExecution.hours || (time.hours >= timeForLastExecution.hours && time.minutes >= timeForLastExecution.minutes)){
			return true;
		}
		
		return false;
	}
	
	public boolean requestExitAfterLossDate(Date date, Position position, ArrayList<StrategyResponse> listOfStrategyResponse){
		if (strategyOptions.maxPositionLossTime == 0){
			return false;
		}
		
		if (((date.getTime() - position.getPositionHistory().dateOfCreation.getTime()) / 60 / 1000) > strategyOptions.maxPositionLossTime && position.getPositionProfitLossAfterComission(true) <= 0){
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

	public boolean stopFromProfitDrawdown(Position position) {
//		Co.print("--> Max Profit was: " +  new DecimalFormat("#.00").format(MathTools.round(position.getPositionHistory().getMaxPercentProfitLoss())));
		
		double profitDrawdown = position.getPositionProfitDrawdown();
		double positionMaxProfitPercent = position.getPositionHistory().getMaxPercentProfitLoss();
		
//		Co.println("--> Drawdown is: " +  positionMaxProfitPercent + ", " + new DecimalFormat("#.00").format(profitDrawdown));
//		Co.println("--> Current profit is: " + new DecimalFormat("#.00").format(MathTools.round(position.getCurrentPercentGainLoss(true))) + "\n");
		
		return profitDrawdown <= strategyOptions.maxProfitDrawdownPercent.value && positionMaxProfitPercent > 0;
	}
}
