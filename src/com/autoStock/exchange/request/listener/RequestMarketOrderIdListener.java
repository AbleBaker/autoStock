/**
 * 
 */
package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultRowMarketOrder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultSetMarketOrder;
import com.autoStock.order.OrderDefinitions.IbOrderStatus;

/**
 * @author Kevin Kowalewski
 * 
 */
public interface RequestMarketOrderIdListener extends RequestListenerBase {
	public void completed(int orderId);
}
