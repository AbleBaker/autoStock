/**
 * 
 */
package com.autoStock.algorithm;

import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmForCiprian extends AlgorithmBase implements ReceiverOfQuoteSlice {
	public AlgorithmForCiprian(boolean canTrade) {
		super(canTrade);
	}

	@Override
	public void receiveQuoteSlice(TypeQuoteSlice quoteSlice) {
		
	}

	@Override
	public void endOfFeed() {
		
	}
}
