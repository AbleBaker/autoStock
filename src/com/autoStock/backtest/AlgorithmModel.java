package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.strategy.StrategyOptions;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmModel {
	public StrategyOptions strategyOptions;
	public ArrayList<SignalParameters> listOfSignalParameters = new ArrayList<SignalParameters>();
	
	public AlgorithmModel(){ }
	
	public AlgorithmModel(StrategyOptions strategyOptions, ArrayList<SignalParameters> listOfSignalParameters) {
		this.strategyOptions = strategyOptions;
		this.listOfSignalParameters = listOfSignalParameters;
	}
}
