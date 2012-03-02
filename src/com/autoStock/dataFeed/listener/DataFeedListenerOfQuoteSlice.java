/**
 * 
 */
package com.autoStock.dataFeed.listener;

import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface DataFeedListenerOfQuoteSlice {
	void receivedQuoteSlice(TypeQuoteSlice typeQuoteSlice);
	void endOfFeed();
}
