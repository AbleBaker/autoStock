package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.backtest.watchmaker.WMAdjustment;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.strategy.StrategyOptions;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmModel {
	public StrategyOptions strategyOptions;
	public ArrayList<SignalParameters> listOfSignalParameters = new ArrayList<SignalParameters>();
	public WMAdjustment wmAdjustment;
	
	public AlgorithmModel(){ }
	
	public AlgorithmModel(StrategyOptions strategyOptions, ArrayList<SignalParameters> listOfSignalParameters) {
		this.strategyOptions = strategyOptions;
		this.listOfSignalParameters = listOfSignalParameters;
	}
	
	public AlgorithmModel copy(){
		AlgorithmModel algorithmModel = new AlgorithmModel();
		
		algorithmModel.strategyOptions = this.strategyOptions.copy();
		algorithmModel.wmAdjustment = this.wmAdjustment.copy();
		
		for (SignalParameters signalParameters : listOfSignalParameters){
			algorithmModel.listOfSignalParameters.add(signalParameters.copy());
		}
		
		return algorithmModel;
	}
	
	public static AlgorithmModel getCurrentAlgorithmModel(AlgorithmBase algorithmBase){
		ArrayList<SignalParameters> listOfSignalParameters = new ArrayList<SignalParameters>();
		
		for (SignalBase signalBase : algorithmBase.signalGroup.getListOfSignalBase()){
			listOfSignalParameters.add(signalBase.signalParameters.copy());
		}
		
		return new AlgorithmModel(algorithmBase.strategyBase.strategyOptions, listOfSignalParameters);
	}
}
