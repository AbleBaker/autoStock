package com.autoStock.strategy;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import com.autoStock.Co;
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
		strategyOptions.minTakeProfitExit = 1.030d;
		strategyOptions.maxStopLossValue = -35;
		strategyOptions.maxNilChangePrice = 15;
		strategyOptions.maxPositionEntryTime = 30;
		strategyOptions.maxPositionTaperTime = 30;
		strategyOptions.maxPositionExitTime = 10;
	}
	
	public StrategyResponse informStrategy(IndicatorGroup indicatorGroup, SignalGroup signalGroup, ArrayList<QuoteSlice> listOfQuoteSlice){
		StrategyResponse strategyResponse = new StrategyResponse();
		QuoteSlice quoteSlice = listOfQuoteSlice.get(listOfQuoteSlice.size()-1);
		Position position = PositionManager.instance.getPosition(algorithmBase.symbol.symbol);
		
		signal.resetAndAddSignalMetrics(signalGroup.signalOfRSI.getSignal()); 
		
		if (algorithmCondition.disableAfterNilChanges(listOfQuoteSlice)){
			strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_nilchange, quoteSlice, position, strategyResponse);
		} //else if (algorithmCondition.disableAfterNilVolume(listOfQuoteSlice)){
//			strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_nilvolume, quoteSlice, position, strategyResponse);
//		}else
		else if (position != null){
			if (algorithmCondition.stopLoss(position)){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_stoploss, quoteSlice, position, strategyResponse);
			}else if (algorithmCondition.takeProfit(position, quoteSlice)){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_profit, quoteSlice, position, strategyResponse);
			}else if (algorithmCondition.requestExitOnDate(quoteSlice.dateTime, algorithmBase.exchange)){
				strategyResponse.positionGovernorResponse = exit(StrategyActionCause.cease_condition_time, quoteSlice, position, strategyResponse);
			}else{
				strategyResponse.positionGovernorResponse = proceed(quoteSlice);
			}
		}else{
			if (algorithmCondition.canEnterTradeOnDate(quoteSlice.dateTime, algorithmBase.exchange) == false){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_time, quoteSlice, position, strategyResponse);
			}else if (algorithmCondition.canTadeAfterTransactions(algorithmBase.algorithmState.transactions) == false){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_trans, quoteSlice, position, strategyResponse);
			}else if (algorithmBase.algorithmState.isDisabled){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_disabled, quoteSlice, position, strategyResponse);
			}else{
				strategyResponse.positionGovernorResponse = proceed(quoteSlice);
			}
		}

		return formulateStrategyResponse(strategyResponse);
	}
	
	public StrategyResponse formulateStrategyResponse(StrategyResponse strategyResponse){
		if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.failed){
			strategyResponse.strategyAction = StrategyAction.algorithm_changed;
			strategyResponse.strategyActionCause = StrategyActionCause.position_governor_failure;
		} else if (strategyResponse.positionGovernorResponse.status != PositionGovernorResponseStatus.none){
			if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry
					|| strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
				
				if (strategyResponse.strategyAction == StrategyAction.none){
					strategyResponse.strategyActionCause = StrategyActionCause.proceed_changed;
					strategyResponse.strategyAction = StrategyAction.algorithm_changed;
				}
			}
		}
				
		if (strategyResponse.strategyAction == lastStrategyResponse.strategyAction && strategyResponse.strategyActionCause == strategyResponse.strategyActionCause){
			strategyResponse.strategyAction = StrategyAction.no_change;
			strategyResponse.strategyActionCause = StrategyActionCause.none;
			return strategyResponse;
		}else{
			lastStrategyResponse = strategyResponse;
			return strategyResponse;	
		}
	}
	
	public PositionGovernorResponse proceed(QuoteSlice quoteSlice){
		PositionGovernorResponse positionGovernorResponse = positionGovener.informGovener(quoteSlice, signal, algorithmBase.exchange, strategyOptions);
		return positionGovernorResponse;
	}
	
	public PositionGovernorResponse cease(StrategyActionCause strategyActionCause, QuoteSlice quoteSlice, Position position, StrategyResponse strategyResponse){
//		Co.println("--> Asked to cease: " + strategyActionCause.name());
		PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
		if (position != null){
			positionGovernorResponse = positionGovener.informGovener(quoteSlice, signal, algorithmBase.exchange, strategyOptions, true);
		}
		strategyResponse.strategyAction = StrategyAction.algorithm_disable;
		strategyResponse.strategyActionCause = strategyActionCause;
		
		return positionGovernorResponse;
	}
	
	public PositionGovernorResponse exit(StrategyActionCause strategyActionCause, QuoteSlice quoteSlice, Position position, StrategyResponse strategyResponse){
		PositionGovernorResponse positionGovernorResponse = positionGovener.informGovener(quoteSlice, signal, algorithmBase.exchange, strategyOptions, true);
		
		strategyResponse.strategyAction = StrategyAction.algorithm_changed;
		strategyResponse.strategyActionCause = strategyActionCause;
		
		return positionGovernorResponse;
	}
}
