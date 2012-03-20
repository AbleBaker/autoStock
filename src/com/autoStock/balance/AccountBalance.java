/**
 * 
 */
package com.autoStock.balance;

/**
 * @author Kevin Kowalewski
 *
 */
public class AccountBalance {
	private static volatile double bankBalance = 10000.00;
	private static volatile double transactionFeesPaid = 0;
	public static AccountBalance instance = new AccountBalance();
	
	public double getBankBalance(){
		return this.bankBalance;
	}
	
	public double getTransactionFeesPaid(){
		return this.transactionFeesPaid;
	}
	
	public void changeBankBalance(double amount){
		this.bankBalance += amount;
	}
	
	public void changeBankBalance(double positionCost, double transactionCost){
		this.bankBalance += positionCost;
		this.bankBalance -= transactionCost;
		this.transactionFeesPaid += transactionCost;
	}
	
	public void setBankBalance(double bankBalance){
		this.bankBalance = bankBalance;
	}
	
	public double getTransactionCost(int units){
		return units * 0.018;
	}
}
