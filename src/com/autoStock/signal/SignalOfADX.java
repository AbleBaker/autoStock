/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfADX extends SignalBase {
	
	public SignalOfADX(){
		super(SignalMetricType.metric_adx);
	}
}
