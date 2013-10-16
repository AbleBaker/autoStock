package com.autoStock.backtest;

import com.autoStock.account.AccountProvider;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestScoreProvider {
	public static double getScore(BacktestEvaluation backtestEvaluation){
		double score;
		
//		if (backtestEvaluation.backtestResultTransactionDetails.countForTradeLongEntry == 0){return 0;}
//		if (backtestEvaluation.backtestResultTransactionDetails.countForTradeShortEntry == 0){return 0;}
//		if (backtestEvaluation.backtestResultTransactionDetails.countForTradesProfit == 0){return 0;} 
		
		score = backtestEvaluation.percentYield; // * (backtestEvaluation.backtestResultTransactionDetails.countForTradeLongEntry + backtestEvaluation.backtestResultTransactionDetails.countForTradeShortEntry);
		
//		score = backtestEvaluation.percentYield * backtestEvaluation.backtestResultTransactionDetails.countForTradeExit;
		
//		score = backtestEvaluation.backtestResultTransactionDetails.avgTradeWin * backtestEvaluation.transactions;
		
		return score;
	}
}
