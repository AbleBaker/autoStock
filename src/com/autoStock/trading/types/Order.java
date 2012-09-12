package com.autoStock.trading.types;

import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class Order {
	private int units;
	private double price;
	private double lastKnownPrice;
	private Symbol symbol;
	private Exchange exchange;
	
	public static enum OrderStatus {
		status_none,
		status_presubmit,
		status_submitted,
		status_filled_partially,
		status_filled,
	}
}
