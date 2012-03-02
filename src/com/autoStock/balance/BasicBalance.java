/**
 * 
 */
package com.autoStock.balance;

/**
 * @author Kevin Kowalewski
 *
 */
public class BasicBalance {
	public static float bankBalance = 25000;
	public static float shares = 0;
	public static int transactions = 0;
	
	public static void buy(float price){
		shares = bankBalance / price;
		transactions++;
	}
	
	public static void sell(float price){
		bankBalance = shares * price;
		transactions++;
	}
}
