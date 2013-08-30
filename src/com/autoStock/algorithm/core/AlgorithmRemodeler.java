package com.autoStock.algorithm.core;

import java.util.List;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalOfADX;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.strategy.StrategyOfTest;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmRemodeler {
	private AlgorithmBase algorithmBase;
	private BacktestEvaluation backtestEvaluation;	
	
	public AlgorithmRemodeler(AlgorithmBase algorithmBase, BacktestEvaluation backtestEvaluation) {
		this.algorithmBase = algorithmBase;
		this.backtestEvaluation = backtestEvaluation;
	}

	public void remodel(){
		algorithmBase.strategyBase.strategyOptions = backtestEvaluation.strategyOptions;
		setSignalBaseParamaters(algorithmBase.signalGroup.getListOfSignalBase(), backtestEvaluation.listOfSignalParameters);
	}
	
	public void setSignalBaseParamaters(List<SignalBase> listOfSignalBase, List<SignalParameters> listOfSignalParameters){
		for (SignalBase signalBase : listOfSignalBase){
			for (SignalParameters signalParameters : listOfSignalParameters){
				if (signalBase.signalParameters.getClass() == signalParameters.getClass()){
					signalBase.signalParameters = signalParameters;
				}
			}
		}
	}
}
