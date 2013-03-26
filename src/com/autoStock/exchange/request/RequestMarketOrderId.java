/**
 * 
 */
package com.autoStock.exchange.request;

import com.autoStock.Co;
import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketOrderIdListener;
import com.autoStock.exchange.request.listener.RequestMarketOrderListener;
import com.autoStock.exchange.results.ExResultMarketOrder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultRowMarketOrder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultSetMarketOrder;
import com.autoStock.order.OrderDefinitions.OrderType;
import com.autoStock.trading.types.Order;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestMarketOrderId {
	private Thread threadForExecution;
	
	public void execute(){
		threadForExecution = new Thread(new Runnable(){
			@Override
			public void run() {
				ExchangeController.getIbExchangeInstance().getNextValidOrderId();	
			}
		});
		
		threadForExecution.start();
	}
	
	public void cancel(){
		try {threadForExecution.interrupt();}catch(Exception e){}
	}
}
