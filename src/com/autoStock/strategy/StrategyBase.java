package com.autoStock.strategy;

import java.util.ArrayList;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.position.PositionGovernor;
import com.autoStock.position.PositionOptions;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalGroup;
import com.autoStock.trading.types.Position;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * 
 */
public abstract class StrategyBase {
	public Signal signal;
	public StrategyOptions strategyOptions;
	public AlgorithmCondition algorithmCondition;
	public AlgorithmBase algorithmBase;
	public final PositionGovernor positionGovener = PositionGovernor.getInstance();
	public StrategyResponse lastStrategyResponse = new StrategyResponse();
	public StrategyResponse currentStrategyResponse = new StrategyResponse();

	public StrategyBase(AlgorithmBase algorithmBase) {
		this.algorithmBase = algorithmBase;
	}
	
	public abstract StrategyResponse informStrategy(IndicatorGroup indicatorGroup, SignalGroup signalGroup, ArrayList<QuoteSlice> listOfQuoteSlice, ArrayList<StrategyResponse> listOfStrategyResponse, Position position, PositionOptions positionOptions);
}
