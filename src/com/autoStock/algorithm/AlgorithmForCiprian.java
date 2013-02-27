/**
 * 
 */
package com.autoStock.algorithm;

import java.util.Date;

import com.autoStock.Co;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.tools.DateTools;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmForCiprian extends AlgorithmBase implements ReceiverOfQuoteSlice {
	public AlgorithmForCiprian(boolean canTrade, Exchange exchange) {
		super(canTrade, exchange, null, AlgorithmMode.mode_backtest, new Date());
	}

	@Override
	public void receiveQuoteSlice(QuoteSlice quoteSlice) {
		Co.println("Received quoteSlice: " + DateTools.getPrettyDate(quoteSlice.dateTime) + ", " + quoteSlice.priceClose);

	}

	@Override
	public void endOfFeed(Symbol symbol) {
		
	}
}
