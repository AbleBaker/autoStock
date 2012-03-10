/**
 * 
 */
package com.autoStock.balance;

/**
 * @author Kevin Kowalewski
 *
 */
public class AccountBalance {
	public static double bankBalance = 25000.00;
	public static AccountBalance instance = new AccountBalance();
	
	public double getBankBalance(){
		return this.bankBalance;
	}
	
	public void setBankBalance(double bankBalance){
		this.bankBalance = bankBalance;
	}
}
