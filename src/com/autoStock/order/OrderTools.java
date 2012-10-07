package com.autoStock.order;

import com.autoStock.exchange.results.ExResultMarketOrder.ExResultRowMarketOrder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultSetMarketOrder;
import com.autoStock.order.OrderDefinitions.IbOrderStatus;
import com.autoStock.order.OrderDefinitions.OrderStatus;

/**
 * @author Kevin Kowalewski
 *
 */
public class OrderTools {
	public IbOrderStatus getOrderStatus(ExResultSetMarketOrder exResultSetMarketOrder){
		for (ExResultRowMarketOrder exResultRowMarketOrder : exResultSetMarketOrder.listOfExResultRowMarketOrder){
			if (exResultRowMarketOrder.status == IbOrderStatus.status_cancelled){
				return IbOrderStatus.status_cancelled;
			}
		}
		
		return IbOrderStatus.unknown;
	}
}
