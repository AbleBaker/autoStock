/**
 * 
 */
package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.RequestHolder;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultSetHistoricalData;
import com.autoStock.trading.results.ExResultMarketData;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestMarketDataListener {
		public void failed(RequestHolder requestHolder);
		public void completed(RequestHolder requestHolder, ExResultMarketData exResultMarketData);
}
