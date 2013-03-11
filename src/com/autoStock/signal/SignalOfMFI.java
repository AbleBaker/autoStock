/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfMFI extends SignalBase {
	
	public SignalOfMFI() {
		super(SignalMetricType.metric_mfi);
	}
}
