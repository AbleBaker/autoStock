package com.autoStock.backtest;

import java.util.Date;

import com.autoStock.account.AccountProvider;
import com.google.gson.internal.Pair;

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
		
		score = backtestEvaluation.percentYield;
		
//		score = backtestEvaluation.percentTradeWin * backtestEvaluation.transactionDetails.countForTradeExit;
		
//		score = backtestEvaluation.transactionDetails.countForTradesProfit;
		
//		score = backtestEvaluation.backtestResultTransactionDetails.avgTradeWin * backtestEvaluation.transactions;
		
		for (Pair<Date, Double> pair : backtestEvaluation.listOfDailyYield){
			if (pair.second <= 0){
				score /= 2;
			}
		}
		
		return score > 0 ? score : 0;
	}
}
