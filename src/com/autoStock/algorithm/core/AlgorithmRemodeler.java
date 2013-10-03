package com.autoStock.algorithm.core;

import java.util.List;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalParameters;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmRemodeler {
	private AlgorithmBase algorithmBase;
	private AlgorithmModel algorithmModel;
	
	public AlgorithmRemodeler(AlgorithmBase algorithmBase, AlgorithmModel algorithmModel) {
		this.algorithmBase = algorithmBase;
		this.algorithmModel = algorithmModel;
	}
	
	public void remodel(){
		remodel(true, true);
	}

	public void remodel(boolean includeStrategyOptions, boolean includeSignalParameters){
		if (includeStrategyOptions){
			algorithmBase.strategyBase.strategyOptions = algorithmModel.strategyOptions;
		}else{
			Co.println("--> Warning: Remodeler discaring strategy options");
		}
		
		if (includeSignalParameters){
			setSignalBaseParamaters(algorithmBase.signalGroup.getListOfSignalBase(), algorithmModel.listOfSignalParameters);
		}else{
			Co.println("--> Warning: Remodeler discarding signal parameters");
		}
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
