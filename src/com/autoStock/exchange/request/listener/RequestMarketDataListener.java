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
public interface RequestMarketDataListener {
		public void failed(RequestHolder requestHolder);
		public void receiveQuoteSlice(RequestHolder requestHolder, QuoteSlice typeQuoteSlice);
		public void completed(RequestHolder requestHolder, ExResultSetMarketData exResultSetMarketData);
}
