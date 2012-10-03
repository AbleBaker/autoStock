package com.autoStock.strategy;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.position.PositionGovernor;
import com.autoStock.signal.Signal;

/**
 * @author Kevin Kowalewski
 * 
 */
public class StrategyBase {
	public Signal signal;
	public StrategyOptions strategyOptions;
	public AlgorithmCondition algorithmCondition;
	public AlgorithmBase algorithmBase;
	public final PositionGovernor positionGovener = PositionGovernor.getInstance();
	public StrategyResponse lastStrategyResponse = new StrategyResponse();

	public StrategyBase(AlgorithmBase algorithmBase) {
		this.algorithmBase = algorithmBase;
	}
}
