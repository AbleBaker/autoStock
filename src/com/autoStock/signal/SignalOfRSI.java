/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfRSI extends SignalBase {

	public SignalOfRSI(){
		super(SignalMetricType.metric_rsi);
	}
}
