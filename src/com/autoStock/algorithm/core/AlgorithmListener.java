/**
 * 
 */
package com.autoStock.algorithm.core;

import com.autoStock.strategy.StrategyResponse;

/**
 * @author Kevin Kowalewski
 *
 */
public interface AlgorithmListener {
	public void receiveStrategyResponse(StrategyResponse strategyResponse);
	public void receiveChangedStrategyResponse(StrategyResponse strategyResponse);
	public void endOfAlgorithm();
}
