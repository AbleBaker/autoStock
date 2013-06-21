/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfMFI extends SignalBase {
	
	public SignalOfMFI(SignalParameters signalParameters) {
		super(SignalMetricType.metric_mfi, signalParameters);
	}
}
