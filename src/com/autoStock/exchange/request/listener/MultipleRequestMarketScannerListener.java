package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.RequestMarketScanner.MarketScannerType;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultSetMarketScanner;
import com.autoStock.exchange.results.MultipleResultMarketScanner.MultipleResultSetMarketScanner;

/**
 * @author Kevin Kowalewski
 *
 */
public interface MultipleRequestMarketScannerListener extends RequestListenerBase {
	public void completed(MultipleResultSetMarketScanner multipleResultSetMarketScanner);
}
