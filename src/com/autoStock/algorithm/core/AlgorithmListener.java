/**
 * 
 */
package com.autoStock.algorithm.core;

import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.signal.Signal;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface AlgorithmListener {
	public void receiveStrategyResponse(StrategyResponse strategyResponse);
	public void receiveChangedStrategyResponse(StrategyResponse strategyResponse);
	public void endOfAlgorithm();
}
