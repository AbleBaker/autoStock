package com.autoStock.backtest;

import java.util.Date;

import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestScoreProvider {
	public static double getScore(BacktestEvaluation backtestEvaluation, boolean allowNegativeScore){
		double score = 0;
		
//		score = getScoreOnlyYield(backtestEvaluation);
		score = getScoreDoDYield(backtestEvaluation);
//		score = getScorePerTrans(backtestEvaluation);
		
		if (allowNegativeScore){return score;}
		else {return score > 0 ? score : 0;}
	}
	
	private static double getScorePerTrans(BacktestEvaluation backtestEvaluation){
		
		double score = 0;
		int penalty = 1;
		
		for (Pair<Date, Double> pair : backtestEvaluation.transactionDetails.listOfTransactionYield){
			score += pair.second;
			if (pair.second < 0){penalty++;}
		}
		
		score /= penalty;
		
		return score;
	}
	
	private static double getScoreDoDYield(BacktestEvaluation backtestEvaluation){
		double score = 0;
		int penalty = 1;
		
		for (Pair<Date, Double> pair : backtestEvaluation.listOfDailyYield){
			score += pair.second;
			if (pair.second < 0){penalty++;}
		}
		
		score /= penalty;
		
		return score;
	}
	
	private static double getScoreOnlyYield(BacktestEvaluation backtestEvaluation){
		return backtestEvaluation.percentYield;
	}
}
