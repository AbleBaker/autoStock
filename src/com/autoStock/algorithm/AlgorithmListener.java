/**
 * 
 */
package com.autoStock.algorithm;

import com.autoStock.signal.Signal;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface AlgorithmListener {
	public void recieveSignal(Signal signal, TypeQuoteSlice typeQuoteSlice);
	public void endOfAlgorithm();
}
