package com.autoStock.backtest;

import com.autoStock.account.AccountProvider;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestScoreProvider {
	public static double getScore(BacktestEvaluation backtestEvaluation){
		return (backtestEvaluation.accountBalance - AccountProvider.getInstance().defaultBalance) * backtestEvaluation.percentTradeWin;
	}
}
