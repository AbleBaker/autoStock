package com.autoStock.account;

/**
 * @author Kevin Kowalewski
 *
 */
public class TransactionFees {
	public static double getTransactionCost(int units, double price) { // TODO: add exchange and security type
		double cost = 0;
		if (units <= 500) {
//			cost = Math.max(1.30, units * 0.013);
			cost = (1.30 >= units * 0.013) ? 1.30 : units * 0.013;
		} else {
			cost = (500 * 0.013) + ((units - 500) * 0.008);
		}

//		return Math.min(cost, units * price * 0.005);
		return (cost <= units * price * 0.005) ? cost : units * price * 0.005;
	}
}
