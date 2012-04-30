/**
 * 
 */
package com.autoStock.finance;


/**
 * @author Kevin Kowalewski
 *
 */
public class Account {
	private volatile double bankBalance = 100000.00;
	private volatile double transactionFeesPaid = 0;
	public static Account instance = new Account();
	
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
	
	public double getTransactionCost(int units, double price){
		if (units <= 500){
			return Math.max(1.30, units * 0.013);	
		}else{
			return Math.max(1.30, units * 0.008);
		}
	}
}
