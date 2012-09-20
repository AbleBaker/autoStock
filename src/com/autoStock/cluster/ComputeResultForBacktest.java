package com.autoStock.cluster;

import java.util.ArrayList;

import com.autoStock.adjust.Iteration;
import com.autoStock.finance.Account;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeResultForBacktest {
	public double accountBalance;
	public int transactions;
	public ArrayList<Iteration> listOfIteration;	
	
	public ComputeResultForBacktest(ArrayList<Iteration> listOfIteration, double accountBalance, int transactions){
		this.listOfIteration = listOfIteration;
		this.accountBalance = accountBalance;
		this.transactions = transactions;
	}
}
