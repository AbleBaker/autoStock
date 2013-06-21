package com.autoStock.backtest;

import com.autoStock.account.AccountProvider;
import com.autoStock.backtest.BacktestUtils.BacktestResultTransactionDetails;

/**
 * @author Kevin Kowalewski
 *
 */
public class BacktestEvaluation {
	public BacktestResultTransactionDetails backtestResultTransactionDetails;
	public double accountBalance;
	public double percentGain;
	public double percentWin;
	public double percentLoss;
	
	public int getScore(){
		return (int) ((accountBalance - AccountProvider.getInstance().defaultBalance) * percentWin);
	}
}
