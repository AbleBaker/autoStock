package com.autoStock.backtest;

import com.autoStock.account.AccountProvider;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestScoreProvider {
	public static double getScore(BacktestEvaluation backtestEvaluation){
		double score;
		score = (backtestEvaluation.accountBalance - AccountProvider.getInstance().defaultBalance) * backtestEvaluation.percentTradeWin;
		return score;
	}
}
