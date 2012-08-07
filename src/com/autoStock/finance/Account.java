/**
 * 
 */
package com.autoStock.finance;


/**
 * @author Kevin Kowalewski
 *
 */
public class Account {
	public static Account instance = new Account();
	private final double bankBalanceDefault = 100000.00;
	private double bankBalance = bankBalanceDefault;
	private final double transactionFeesDefault = 0;
	private double transactionFeesPaid = 0;
	private int transactions = 0;
	
	public double getBankBalance(){
		synchronized (this) {
			return this.bankBalance;	
		}
	}
	
	public double getTransactionFeesPaid(){
		return this.transactionFeesPaid;
	}
	
	public int getTransactions(){
		return this.transactions;
	}
	
	private void changeBankBalance(double amount){
		this.bankBalance += amount;
	}
	
	public synchronized void changeBankBalance(double positionCost, double transactionCost){
		synchronized (this) {
			bankBalance += positionCost;
			bankBalance -= transactionCost;
			transactionFeesPaid += transactionCost;
			transactions++;
		}
	}
	
	public synchronized double getTransactionCost(int units, double price){
		double cost = 0;
		if (units <= 500){
			cost = Math.max(1.30, units * 0.013);	
		}else{
			cost = Math.max(1.30, units * 0.008);
		}
		
		return Math.min(cost, units * price * 0.005);
	}
	
	public synchronized void resetAccount(){
		synchronized(this){
			bankBalance = bankBalanceDefault;
			transactionFeesPaid = transactionFeesDefault;
			transactions = 0;
		}
	}
}
