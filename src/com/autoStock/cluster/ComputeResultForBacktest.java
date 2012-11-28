package com.autoStock.cluster;

import java.util.ArrayList;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeResultForBacktest {
	public double accountBalance;
	public int transactions;
	public ArrayList<?> listOfAdjustment;
	public int requestId;
	public int unitId;
	public String resultDetails;
	
	public ComputeResultForBacktest(int requestId, int unitId, ArrayList<?> listOfAdjustment, double accountBalance, int transactions, String resultDetails){
		this.requestId = requestId;
		this.unitId = unitId;
		this.listOfAdjustment = listOfAdjustment;
		this.accountBalance = accountBalance;
		this.transactions = transactions;
		this.resultDetails = resultDetails;
	}
}
