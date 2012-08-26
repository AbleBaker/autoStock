/**
 * 
 */
package com.autoStock.algorithm.core;

import com.autoStock.position.PGResponse;
import com.autoStock.signal.Signal;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface AlgorithmListener {
	public void receivePositionGovernorResponse(PGResponse positionGovernorResponse);
	public void recieveSignal(Signal signal, QuoteSlice typeQuoteSlice);
	public void endOfAlgorithm();
}
