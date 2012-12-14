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
	public double percentOfTransProfitLoss;
	public ArrayList<AdjustmentOfPortable> listOfAdjustment;
	public int unitId;
	public String resultDetails;
	
	public ComputeResultForBacktestPartial(int unitId, ArrayList<AdjustmentOfPortable> listOfAdjustmentOfPortable, double accountBalance, double percentOfTransProfitLoss, int transactions, String resultDetails){
		this.unitId = unitId;
		this.listOfAdjustment = listOfAdjustmentOfPortable;
		this.accountBalance = accountBalance;
		this.transactions = transactions;
		this.percentOfTransProfitLoss = percentOfTransProfitLoss;
		this.resultDetails = resultDetails;
	}
}
