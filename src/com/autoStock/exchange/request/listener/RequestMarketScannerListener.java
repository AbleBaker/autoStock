/**
 * 
 */
package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.results.ExResultMarketData;
import com.autoStock.exchange.results.ExResultHistoricalData.ExResultSetHistoricalData;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultSetMarketScanner;
import com.autoStock.exchange.results.ExResultRealtimeData.ExResultSetRealtimeData;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestMarketScannerListener {
		public void failed(RequestHolder requestHolder);
		public void completed(RequestHolder requestHolder, ExResultSetMarketScanner exResultSetMarketScanner);
}
