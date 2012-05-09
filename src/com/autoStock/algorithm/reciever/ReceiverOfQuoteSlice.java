/**
 * 
 */
package com.autoStock.algorithm.reciever;

import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface ReceiverOfQuoteSlice {
	void receiveQuoteSlice(QuoteSlice quoteSlice);
	void endOfFeed();
}
