/**
 * 
 */
package com.autoStock.dataFeed.listener;

import com.autoStock.exchange.results.ResultQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface DataFeedListenerOfQuoteSlice {
	void receivedQuoteSlice(ResultQuoteSlice resultQuoteSlice);
}
