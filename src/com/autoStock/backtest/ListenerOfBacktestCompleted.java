package com.autoStock.backtest;

import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public interface ListenerOfBacktestCompleted {
	public void backtestCompleted(Symbol symbol);
}