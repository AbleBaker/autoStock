/**
 * 
 */
package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultSetHistoricalData;
import com.autoStock.trading.results.ExResultMarketData;
import com.autoStock.trading.results.ExResultMarketData.ExResultSetMarketData;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestMarketDataListener {
		public void failed(RequestHolder requestHolder);
		public void completed(RequestHolder requestHolder, ExResultSetMarketData exResultSetMarketData);
}
