package com.autoStock.order;

import com.autoStock.trading.types.Order;

/**
 * @author Kevin Kowalewski
 *
 */
public class OrderSimulator {
	private Thread threadForOrderSimulator;
	private int slippage = 0;
	private Order order;
	
	public OrderSimulator(Order order){
		this.order = order;
	}
	
	public void simulateOrderFill(){
		
		order.orderUnitsFilled(order.getUnitPriceRequested(), order.getUnitsRequested());
		
//		threadForOrderSimulator = new Thread(new Runnable(){
//			@Override
//			public void run() {
//			}
//		});
//		
//		threadForOrderSimulator.start();
	}
}
