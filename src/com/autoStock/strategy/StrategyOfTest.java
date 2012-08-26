package com.autoStock.strategy;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseStatus;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalGroup;
import com.autoStock.signal.SignalPointMethod.SignalPointTactic;
import com.autoStock.strategy.StrategyResponse.StrategyAction;
import com.autoStock.strategy.StrategyResponse.StrategyActionCause;
import com.autoStock.trading.types.Position;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyOfTest extends StrategyBase {
	public StrategyOfTest(AlgorithmBase algorithmBase){
		super(algorithmBase);
		
		signal = new Signal(SignalSource.from_algorithm);
		strategyOptions = new StrategyOptions();
		algorithmCondition = new AlgorithmCondition(strategyOptions);
		
		strategyOptions.canGoLong = true;
		strategyOptions.canGoShort = false;
		strategyOptions.disableAfterNilChanges = true;
		strategyOptions.taperPeriodLength = true;
		strategyOptions.signalPointTactic = SignalPointTactic.tatic_change;
		
		strategyOptions.maxTransactionsDay = 4;
		strategyOptions.minTakeProfitExit = 1.020d;
		strategyOptions.maxStopLossValue = -35;
		strategyOptions.maxNilChanges = 16;
		strategyOptions.maxPositionEntryTime = 30;
		strategyOptions.maxPositionTaperTime = 30;
		strategyOptions.maxPositionExitTime = 8;
	}
	
	public StrategyResponse informStrategy(IndicatorGroup indicatorGroup, SignalGroup signalGroup, ArrayList<QuoteSlice> listOfQuoteSlice){
		StrategyResponse strategyResponse = new StrategyResponse();
		QuoteSlice quoteSlice = listOfQuoteSlice.get(listOfQuoteSlice.size()-1);
		Position position = PositionManager.instance.getPosition(algorithmBase.symbol.symbol);
		
		signal.resetAndAddSignalMetrics(signalGroup.signalOfDI.getSignal(), signalGroup.signalOfRSI.getSignal());
		
		if (algorithmCondition.disableAfterNilChanges(listOfQuoteSlice)){
			strategyResponse = cease(StrategyActionCause.cease_algorithm_condition_nilchange, quoteSlice, position, strategyResponse);
		}
		
		else if (algorithmCondition.canTradeOnDate(quoteSlice.dateTime, algorithmBase.exchange) == false){
			strategyResponse = cease(StrategyActionCause.cease_external_condition_time, quoteSlice, position, strategyResponse);
		}

		else if (algorithmCondition.canTadeAfterTransactions(algorithmBase.algorithmState.transactions) == false){
			strategyResponse = cease(StrategyActionCause.cease_algorithm_condition_trans, quoteSlice, position, strategyResponse);
		}
		
		else if (algorithmBase.algorithmState.isDisabled){
			strategyResponse = cease(StrategyActionCause.cease_algorithm_disabled, quoteSlice, position, strategyResponse);
		}
		
		else if (position != null){
			if (algorithmCondition.stopLoss(position)){ //Cond stop loss
				cease(StrategyActionCause.cease_algorithm_condition_stoploss, quoteSlice, position, strategyResponse);
			}
			
			if (algorithmCondition.takeProfit(position, quoteSlice)){ //Cond profit
				cease(StrategyActionCause.cease_algorithm_condition_profit, quoteSlice, position, strategyResponse);
			}
			
			if (algorithmCondition.requestExitOnDate(quoteSlice.dateTime, algorithmBase.exchange)){  //Cond time
				exit();
			}
		}
		
		else{
			strategyResponse.positionGovernorResponse = proceed(quoteSlice);
		}
		
		return formulateStrategyResponse(strategyResponse);
	}
	
	public StrategyResponse formulateStrategyResponse(StrategyResponse strategyResponse){
		if (strategyResponse.positionGovernorResponse.status != PositionGovernorResponseStatus.none){
			if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.status_changed_long_entry
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.status_changed_long_exit
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.status_changed_short_entry
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.status_changed_short_exit){
				
				strategyResponse.strategyAction = StrategyAction.algorithm_proceed;
				
			}
		}
		
		lastStrategyResponse = strategyResponse;
		
		return strategyResponse;
	}
	
	public PositionGovernorResponse proceed(QuoteSlice quoteSlice){ //Allowed to inform Governor
		PositionGovernorResponse positionGovernorResponse = positionGovener.informGovener(quoteSlice, signal, algorithmBase.exchange, strategyOptions);
		return positionGovernorResponse;
	}
	
	public StrategyResponse cease(StrategyActionCause strategyActionCause, QuoteSlice quoteSlice, Position position, StrategyResponse strategyResponse){ //Disable and/or exit
		strategyResponse.strategyAction = StrategyAction.algorithm_disable;
		
		if (strategyResponse.strategyAction != lastStrategyResponse.strategyAction || strategyActionCause != lastStrategyResponse.strategyActionCause){
			strategyResponse.strategyActionCause = strategyActionCause;
		}
		return strategyResponse;
	}
	
	public void exit(){
		
	}
}
