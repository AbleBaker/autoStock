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
	public static AccountBalance instance = new AccountBalance();
	
	public double getBankBalance(){
		return this.bankBalance;
	}
	
	public void changeBankBalance(double amount){
		this.bankBalance += amount;
	}
	
	public void setBankBalance(double bankBalance){
		this.bankBalance = bankBalance;
	}
}
