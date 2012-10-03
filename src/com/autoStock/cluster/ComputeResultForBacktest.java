package com.autoStock.cluster;

import java.util.ArrayList;

import com.autoStock.adjust.Iteration;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeResultForBacktest {
	public double accountBalance;
	public int transactions;
	public ArrayList<Iteration> listOfIteration;
	public int requestId;
	public int unitId;
	public String resultDetails;
	
	public ComputeResultForBacktest(int requestId, int unitId, ArrayList<Iteration> listOfIteration, double accountBalance, int transactions, String resultDetails){
		this.requestId = requestId;
		this.unitId = unitId;
		this.listOfIteration = listOfIteration;
		this.accountBalance = accountBalance;
		this.transactions = transactions;
		this.resultDetails = resultDetails;
	}
}
