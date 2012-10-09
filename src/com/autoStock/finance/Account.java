/**
 * 
 */
package com.autoStock.finance;

import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.Co;
import com.autoStock.tools.MathTools;
import com.google.common.util.concurrent.AtomicDouble;


/**
 * @author Kevin Kowalewski
 *
 */
public class Account {
	private static Account instance = new Account();
	public final double bankBalanceDefault = 100000.00;
	private final double transactionFeesDefault = 0;
	private AtomicDouble bankBalance = new AtomicDouble();
	private AtomicDouble transactionFeesPaid = new AtomicDouble();
	private AtomicInteger transactions = new AtomicInteger();
	
	private Account(){
		resetAccount();
	}
	
	public static Account getInstance(){
		return instance;
	}
	
	public synchronized double getAccountBalance(){
		synchronized (this) {
			return MathTools.round(bankBalance.get());
		}
	}
	
	public synchronized double getTransactionFeesPaid(){
		synchronized (this) {
			return MathTools.round(transactionFeesPaid.get());
		}
	}
	
	public synchronized int getTransactions(){
		synchronized (this) {
			return transactions.get();	
		}
	}
	
	public synchronized void changeAccountBalance(double positionCost, double transactionCost){
		synchronized (this) {
			
			Co.println("--> Changing account balance by: " + positionCost + ", " + transactionCost + ", " + (positionCost + transactionCost));
			
			bankBalance.addAndGet(positionCost);
			bankBalance.addAndGet(transactionCost *-1);
			transactionFeesPaid.getAndAdd(transactionCost);
			transactions.incrementAndGet();
		}
	}
	
	public synchronized double getTransactionCost(int units, double price){ //TODO: SecurityType securityType, Exchange exchange
		synchronized (this) {
			double cost = 0;
			if (units <= 500){
				cost = Math.max(1.30, units * 0.013);	
			}else{
				cost = (500 * 0.013) + ((units - 500) * 0.008);
			}
			
			return Math.min(cost, units * price * 0.005);
		}
	}
	
	public synchronized void resetAccount(){
		synchronized(this){
			bankBalance.set(bankBalanceDefault);
			transactionFeesPaid.set(transactionFeesDefault);
			transactions.set(0);
		}
	}
}
