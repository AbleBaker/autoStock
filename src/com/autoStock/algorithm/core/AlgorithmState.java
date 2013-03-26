package com.autoStock.algorithm.core;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmState {
	public boolean isDisabled = false;
	public boolean canTrade = false;
	public int transactions = 0;
	public String disabledReason;
}
