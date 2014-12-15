package com.autoStock.backtest;

import java.util.Date;

import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestScoreProvider {
	public static double getScore(BacktestEvaluation backtestEvaluation){
		double score = 0;
//		score = backtestEvaluation.percentYield;
//		score = backtestEvaluation.percentTradeWin * backtestEvaluation.transactionDetails.countForTradeExit;
//		score = backtestEvaluation.transactionDetails.countForTradesProfit * backtestEvaluation.percentYield;
//		score = backtestEvaluation.percentTradeProfit * (backtestEvaluation.percentYield * 2);

		int penalty = 1;
		
		for (Pair<Date, Double> pair : backtestEvaluation.listOfDailyYield){
			score += pair.second;
			//score += Math.min(pair.second, 2);
			if (pair.second < 0){penalty++;}
		}
		
//		penalty += backtestEvaluation.transactionDetails.countForTradesLoss;
		
		score /= penalty;
		
		return score > 0 ? score : 0;
	}
}
