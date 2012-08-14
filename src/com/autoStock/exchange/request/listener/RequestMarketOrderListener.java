/**
 * 
 */
package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultRowMarketOrder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultSetMarketOrder;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * 
 */
public interface RequestMarketOrderListener extends RequestListenerBase {
	public void receivedOrder(RequestHolder requestHolder, ExResultRowMarketOrder exResultRowMarketOrder);
	public void completed(RequestHolder requestHolder, ExResultSetMarketOrder exResultSetMarketOrder);
	public void failed();
}
