/**
 * 
 */
package com.autoStock.algorithm;

import com.autoStock.signal.Signal;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface AlgorithmListener {
	public void recieveSignal(Signal signal, QuoteSlice typeQuoteSlice);
	public void endOfAlgorithm();
}
