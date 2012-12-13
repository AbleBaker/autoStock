package com.autoStock.cluster;

import java.util.ArrayList;

import com.autoStock.adjust.AdjustmentOfPortable;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeResultForBacktestPartial {
	public double accountBalance;
	public int transactions;
	public ArrayList<AdjustmentOfPortable> listOfAdjustment;
	public int unitId;
	public String resultDetails;
	
	public ComputeResultForBacktestPartial( int unitId, ArrayList<AdjustmentOfPortable> listOfAdjustmentOfPortable, double accountBalance, int transactions, String resultDetails){
		this.unitId = unitId;
		this.listOfAdjustment = listOfAdjustmentOfPortable;
		this.accountBalance = accountBalance;
		this.transactions = transactions;
		this.resultDetails = resultDetails;
	}
}
