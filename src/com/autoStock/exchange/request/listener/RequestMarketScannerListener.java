/**
 * 
 */
package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultSetMarketScanner;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestMarketScannerListener {
		public void failed(RequestHolder requestHolder);
		public void completed(RequestHolder requestHolder, ExResultSetMarketScanner exResultSetMarketScanner);
}
