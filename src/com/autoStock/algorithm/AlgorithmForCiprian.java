/**
 * 
 */
package com.autoStock.algorithm;

import com.autoStock.Co;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.tools.DateTools;
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
		Co.println("Received quoteSlice: " + DateTools.getPrettyDate(quoteSlice.dateTime) + ", " + quoteSlice.priceClose);

	}

	@Override
	public void endOfFeed() {
		
	}
}
