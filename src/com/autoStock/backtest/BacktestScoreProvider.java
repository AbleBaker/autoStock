package com.autoStock.backtest;

import com.autoStock.account.AccountProvider;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestScoreProvider {
	public static double getScore(BacktestEvaluation backtestEvaluation){
		double score;
		score = backtestEvaluation.percentYield * backtestEvaluation.backtestResultTransactionDetails.countForTradeEntry; //(backtestEvaluation.accountBalance - AccountProvider.defaultBalance); // * backtestEvaluation.percentTradeWin; // * backtestEvaluation.transactions;
		
//		score = backtestEvaluation.percentYield * backtestEvaluation.backtestResultTransactionDetails.countForTradeExit;
		
//		score = backtestEvaluation.backtestResultTransactionDetails.avgTradeWin * backtestEvaluation.transactions;
		
		return score;
	}
}
