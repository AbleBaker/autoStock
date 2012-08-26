package com.autoStock.strategy;

import java.util.ArrayList;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.position.PGResponse;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalGroup;
import com.autoStock.signal.SignalPointMethod.SignalPointTactic;
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
		algorithmCondition = new AlgorithmCondition();
		strategyOptions = new StrategyOptions();
		signalPointTactic = SignalPointTactic.tatic_change;
		
		strategyOptions.canGoLong = true;
		strategyOptions.canGoShort = false;
		strategyOptions.disableAfterNilChanges = true;
		strategyOptions.taperPeriodLength = true;
	}
	
	public StrategyResponse informStrategy(IndicatorGroup indicatorGroup, SignalGroup signalGroup, ArrayList<QuoteSlice> listOfQuoteSlice){
		StrategyResponse strategyResponse = new StrategyResponse();
		QuoteSlice quoteSlice = listOfQuoteSlice.get(listOfQuoteSlice.size()-1);
		Position position = PositionManager.instance.getPosition(algorithmBase.symbol.symbol);
		
		signal.resetAndAddSignalMetrics(signalGroup.signalOfDI.getSignal(), signalGroup.signalOfRSI.getSignal());
		
		if (algorithmCondition.disableAfterNilChanges(listOfQuoteSlice)){
			cease(StrategyActionCause.cease_algorithm_condition_nilchange, quoteSlice, position);
		}
		
		if (algorithmCondition.canTradeOnDate(quoteSlice.dateTime, algorithmBase.exchange) == false){
			cease(StrategyActionCause.cease_external_condition_time, quoteSlice, position);
		}

		if (algorithmCondition.canTadeAfterTransactions(algorithmBase.algorithmState.transactions) == false){
			cease(StrategyActionCause.cease_algorithm_condition_trans, quoteSlice, position);
		}
		
		if (algorithmBase.algorithmState.isDisabled){
			cease(StrategyActionCause.cease_algorithm_disabled, quoteSlice, position);
		}
		
		if (position != null){
			if (algorithmCondition.stopLoss(position)){ //Cond stop loss
				cease(StrategyActionCause.cease_algorithm_condition_stoploss, quoteSlice, position);
			}
			
			if (algorithmCondition.takeProfit(position, quoteSlice)){ //Cond profit
				cease(StrategyActionCause.cease_algorithm_condition_profit, quoteSlice, position);
			}
			
			if (algorithmCondition.requestExitOnDate(quoteSlice.dateTime, algorithmBase.exchange)){  //Cond time
				exit();
			}
		}
		
		proceed(quoteSlice);
		
		return strategyResponse;
	}
	
	public void proceed(QuoteSlice quoteSlice){ //Allowed to inform Governor
		PGResponse PGResponse = positionGovener.informGovener(quoteSlice, signal, algorithmBase.exchange);
	}
	
	public void cease(StrategyActionCause strategyActionCause, QuoteSlice quoteSlice, Position position){ //Disable and/or exit
		
	}
	
	public void exit(){
		
	}
}
