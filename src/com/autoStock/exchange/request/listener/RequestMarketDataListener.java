/**
 * 
 */
package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestMarketDataListener extends RequestListenerBase {
		public void receiveQuoteSlice(RequestHolder requestHolder, QuoteSlice quoteSlice);
		public void completed(RequestHolder requestHolder, ExResultSetMarketData exResultSetMarketData);
}
