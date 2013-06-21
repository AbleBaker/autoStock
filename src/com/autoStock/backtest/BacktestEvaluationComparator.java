package com.autoStock.backtest;

import java.util.Comparator;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluationComparator implements Comparator<BacktestEvaluation> {

	@Override
	public int compare(BacktestEvaluation backtestEvaluation1, BacktestEvaluation backtestEvaluation2) {
		int score1 = backtestEvaluation1.getScore();
		int score2 = backtestEvaluation2.getScore();
		
		if (score1 ==  score2){return 0;}
		else if (score1 < score2){return 1;}
		else if (score2 > score2){return -1;}
		else {throw new IllegalStateException();}
	}
}
