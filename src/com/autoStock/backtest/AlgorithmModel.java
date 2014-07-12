package com.autoStock.backtest;

import java.util.ArrayList;

import org.jfree.chart.HashUtilities;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.backtest.watchmaker.WMAdjustment;
import com.autoStock.indicator.IndicatorBase;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.IndicatorParameters;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.tools.MiscTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmModel {
	public StrategyOptions strategyOptions;
	public ArrayList<SignalParameters> listOfSignalParameters = new ArrayList<SignalParameters>();
	public ArrayList<IndicatorParameters> listOfIndicatorParameters = new ArrayList<IndicatorParameters>();
	public WMAdjustment wmAdjustment;
	public int periodLength;
	
	public AlgorithmModel(){ }
	
	public AlgorithmModel(StrategyOptions strategyOptions, ArrayList<SignalParameters> listOfSignalParameters, ArrayList<IndicatorParameters> listOfIndicatorParameters, int periodLength) {
		this.strategyOptions = strategyOptions;
		this.listOfSignalParameters = listOfSignalParameters;
		this.listOfIndicatorParameters = listOfIndicatorParameters;
		this.periodLength = periodLength;
	}
	
	public AlgorithmModel copy(){
		AlgorithmModel algorithmModel = new AlgorithmModel();
		
		algorithmModel.strategyOptions = strategyOptions.copy();
		algorithmModel.wmAdjustment = wmAdjustment.copy();
		algorithmModel.periodLength = periodLength;
		
		for (SignalParameters signalParameters : listOfSignalParameters){
			algorithmModel.listOfSignalParameters.add(signalParameters.copy());
		}
		
		for (IndicatorParameters indicatorParameters : listOfIndicatorParameters){
			algorithmModel.listOfIndicatorParameters.add(indicatorParameters.copy());
		}
		
		return algorithmModel;
	}
	
	public static AlgorithmModel getCurrentAlgorithmModel(AlgorithmBase algorithmBase){
		ArrayList<SignalParameters> listOfSignalParameters = new ArrayList<SignalParameters>();
		ArrayList<IndicatorParameters> listOfIndicatorParameters = new ArrayList<IndicatorParameters>();
		
		for (SignalBase signalBase : algorithmBase.signalGroup.getListOfSignalBase()){
			listOfSignalParameters.add(signalBase.signalParameters.copy());
		}
		
		for (IndicatorBase indicatorBase : algorithmBase.indicatorGroup.getListOfIndicatorBase()){
			listOfIndicatorParameters.add(indicatorBase.indicatorParameters.copy());
		}
		
		return new AlgorithmModel(algorithmBase.strategyBase.strategyOptions, listOfSignalParameters, listOfIndicatorParameters, algorithmBase.getPeriodLength());
	}
	
	public String getUniqueIdentifier(){
		GsonBuilder builder = new GsonBuilder().serializeSpecialFloatingPointValues();
		return MiscTools.getHash(builder.create().toJson(this));
	}
}
