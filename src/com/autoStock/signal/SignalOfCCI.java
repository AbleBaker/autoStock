/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCCI extends SignalBase {
	
	public SignalOfCCI(){
		super(SignalMetricType.metric_cci);
	}
}
