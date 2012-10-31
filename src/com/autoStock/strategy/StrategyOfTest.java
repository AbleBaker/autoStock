package com.autoStock.strategy;

import java.util.ArrayList;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseStatus;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.SignalPointMethod;
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
		strategyOptions = new StrategyOptions();
		algorithmCondition = new AlgorithmCondition(strategyOptions);
		
		strategyOptions.canGoLong = true;
		strategyOptions.canGoShort = false;
		strategyOptions.canReenter = false;
		strategyOptions.mustHavePositiveSlice = true;
		strategyOptions.disableAfterNilChanges = true;
		strategyOptions.disableAfterNilVolumes = true;
		strategyOptions.disableAfterLoss = false;
		strategyOptions.taperPeriodLength = true;
		strategyOptions.signalPointTacticForEntry = SignalPointTactic.tatic_change;
		strategyOptions.signalPointTacticForReentry = SignalPointTactic.tatic_change;
		strategyOptions.signalPointTacticForExit = SignalPointTactic.tatic_change;

		strategyOptions.maxTransactionsDay = 32;
		strategyOptions.minTakeProfitExit = 1.98d;
		strategyOptions.maxStopLossValue = -50;
		strategyOptions.maxNilChangePrice = 15;
		strategyOptions.maxNilChangeVolume = 15;
		strategyOptions.maxPositionEntryTime = 30;
		strategyOptions.maxPositionExitTime = 10;
		strategyOptions.maxPositionTaperTime = 30;
		strategyOptions.maxReenterTimes = 5;
		strategyOptions.intervalForReentryMins = 10;
		strategyOptions.minReentryPercentGain = 0.20;
	}
	
	public StrategyResponse informStrategy(IndicatorGroup indicatorGroup, SignalGroup signalGroup, ArrayList<QuoteSlice> listOfQuoteSlice, ArrayList<StrategyResponse> listOfStrategyResponse){
		StrategyResponse strategyResponse = new StrategyResponse();
		QuoteSlice quoteSlice = listOfQuoteSlice.get(listOfQuoteSlice.size()-1);
		Position position = PositionManager.getInstance().getPosition(algorithmBase.symbol);
		
		signal = new Signal(SignalSource.from_algorithm, signalGroup);
		signal.resetAndAddSignalMetrics(
				signalGroup.signalOfCCI.getSignal()
//				signalGroup.signalOfRSI.getSignal(),
//				signalGroup.signalOfDI.getSignal()
//				signalGroup.signalOfMACD.getSignal()
//				signalGroup.signalOfMFI.getSignal()
//				signalGroup.signalOfTRIX.getSignal()
//				signalGroup.signalOfROC.getSignal()
//				signalGroup.signalOfWILLR.getSignal()
				);
		
		SignalPoint signalPointForEntry = SignalPointMethod.getSignalPoint(false, signal, PositionType.position_none, strategyOptions.signalPointTacticForEntry);
		
		if (algorithmBase.algorithmState.isDisabled){
			strategyResponse.positionGovernorResponse = new PositionGovernorResponse();
			strategyResponse.strategyAction = StrategyAction.algorithm_disable;
			strategyResponse.strategyActionCause = StrategyActionCause.cease_disabled;
		}
		else if (algorithmCondition.disableAfterNilChanges(listOfQuoteSlice)){
			strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_nilchange, quoteSlice, position, strategyResponse);
		}
		else if (algorithmCondition.disableAfterNilVolume(listOfQuoteSlice)){
			strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_nilvolume, quoteSlice, position, strategyResponse);
		}
		else if (position != null){
			if (algorithmCondition.stopLoss(position)){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_stoploss, quoteSlice, position, strategyResponse);
			}
			else if (algorithmCondition.takeProfit(position, quoteSlice)){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_profit, quoteSlice, position, strategyResponse);
			}
			else if (algorithmCondition.requestExitOnDate(quoteSlice.dateTime, algorithmBase.exchange)){
				strategyResponse.positionGovernorResponse = exit(StrategyActionCause.cease_condition_time_exit, quoteSlice, position, strategyResponse);
			}else{
				strategyResponse.positionGovernorResponse = proceed(quoteSlice);
			}
		}else{
			if (algorithmCondition.canEnterTradeOnDate(quoteSlice.dateTime, algorithmBase.exchange) == false){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_time_entry, quoteSlice, position, strategyResponse);
			}
			else if (algorithmCondition.canTadeAfterTransactions(algorithmBase.algorithmState.transactions) == false){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_trans, quoteSlice, position, strategyResponse);
			}
			else if (strategyOptions.disableAfterLoss && algorithmCondition.canTradeAfterLoss(listOfStrategyResponse) == false){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_condition_loss, quoteSlice, position, strategyResponse);
			}
			else if (algorithmBase.algorithmState.isDisabled){
				strategyResponse.positionGovernorResponse = cease(StrategyActionCause.cease_disabled, quoteSlice, position, strategyResponse);
			}
			else if (algorithmCondition.canEnterWithQuoteSlice(quoteSlice, signalPointForEntry) == false){
				strategyResponse.positionGovernorResponse = new PositionGovernorResponse();
				strategyResponse.strategyAction = StrategyAction.algorithm_pass;
				strategyResponse.strategyActionCause = StrategyActionCause.pass_condition_quotslice;
			}
			else{
				strategyResponse.positionGovernorResponse = proceed(quoteSlice);
			}
		}
		
		strategyResponse.signal = signal;
		strategyResponse.quoteSlice = quoteSlice;

		return formulateStrategyResponse(strategyResponse);
	}
	
	private StrategyResponse formulateStrategyResponse(StrategyResponse strategyResponse){
		if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.failed){
			strategyResponse.strategyAction = StrategyAction.algorithm_changed;
			strategyResponse.strategyActionCause = StrategyActionCause.position_governor_failure;
		} else if (strategyResponse.positionGovernorResponse.status != PositionGovernorResponseStatus.none){
			if (didPositionGovernorChangePosition(strategyResponse.positionGovernorResponse)){
				if (strategyResponse.strategyAction == StrategyAction.none){
					strategyResponse.strategyActionCause = StrategyActionCause.proceed_changed;
					strategyResponse.strategyAction = StrategyAction.algorithm_changed;
				}else{
					//?
				}
			}
		}
		
//		Co.println("--> Last: " + lastStrategyResponse.strategyAction + ", " + lastStrategyResponse.strategyActionCause + ", " + lastStrategyResponse.positionGovernorResponse.signalPoint.name());
//		Co.println("--> Current: " + strategyResponse.strategyAction + ", " + strategyResponse.strategyActionCause + ", " + strategyResponse.positionGovernorResponse.signalPoint.name());
		currentStrategyResponse = strategyResponse.clone();
//				
		if (strategyResponse.strategyAction == lastStrategyResponse.strategyAction && strategyResponse.strategyActionCause == strategyResponse.strategyActionCause && didPositionGovernorChangePosition(strategyResponse.positionGovernorResponse) == false){
			strategyResponse.strategyAction = StrategyAction.no_change;
			strategyResponse.strategyActionCause = StrategyActionCause.none;
			return strategyResponse;
		}else{
			lastStrategyResponse = strategyResponse;
			return strategyResponse;
		}
	}
	
	private PositionGovernorResponse proceed(QuoteSlice quoteSlice){
//		Co.println("--> Asked to proceed");
		PositionGovernorResponse positionGovernorResponse = positionGovener.informGovener(quoteSlice, signal, algorithmBase.exchange, strategyOptions);
		return positionGovernorResponse;
	}
	
	private PositionGovernorResponse cease(StrategyActionCause strategyActionCause, QuoteSlice quoteSlice, Position position, StrategyResponse strategyResponse){
//		Co.println("--> Asked to cease: " + strategyActionCause.name());
		PositionGovernorResponse positionGovernorResponse = new PositionGovernorResponse();
		if (position != null){
			positionGovernorResponse = positionGovener.informGovener(quoteSlice, signal, algorithmBase.exchange, strategyOptions, true);
		}
		strategyResponse.strategyAction = StrategyAction.algorithm_disable;
		strategyResponse.strategyActionCause = strategyActionCause;
		
		return positionGovernorResponse;
	}
	
	private PositionGovernorResponse exit(StrategyActionCause strategyActionCause, QuoteSlice quoteSlice, Position position, StrategyResponse strategyResponse){
//		Co.println("--> Asked to exit");
		PositionGovernorResponse positionGovernorResponse = positionGovener.informGovener(quoteSlice, signal, algorithmBase.exchange, strategyOptions, true);
		
		strategyResponse.strategyAction = StrategyAction.algorithm_changed;
		strategyResponse.strategyActionCause = strategyActionCause;
		
		return positionGovernorResponse;
	}
	
	private boolean didPositionGovernorChangePosition(PositionGovernorResponse positionGovernorResponse){
		if (positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry
				|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry
				|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit
				|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry
				|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry
				|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
			return true;
		}else{
			return false;
		}
	}
}
