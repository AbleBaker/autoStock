/**
 * 
 */
package com.autoStock.algorithm.reciever;

import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface ReceiverOfQuoteSlice {
	void receiveQuoteSlice(TypeQuoteSlice quoteSlice);
	void endOfFeed();
}
