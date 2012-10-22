/**
 * 
 */
package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.results.ExResultMarketSymbolData.ExResultSetMarketSymbolData;
import com.autoStock.exchange.results.ExResultMarketIndexData.ExResultSetMarketIndexData;
import com.autoStock.types.IndexSlice;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestMarketIndexDataListener extends RequestListenerBase {
	public void receiveIndexSlice(RequestHolder requestHolder, IndexSlice indexSlice);
	public void completed(RequestHolder requestHolder, ExResultSetMarketIndexData exResultSetIndexData);
}
