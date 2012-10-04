package com.autoStock.order;

import com.autoStock.trading.types.Order;
import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class OrderManager {
	private static OrderManager instance = new OrderManager();
	
	public static OrderManager getInstance(){
		return instance;
	}
	
	
	public synchronized void submitOrder(Position position, Order order){
		
	}
}
