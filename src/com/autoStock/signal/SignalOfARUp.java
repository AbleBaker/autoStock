/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfARUp extends SignalBase {
	
	public SignalOfARUp(){
		super(SignalMetricType.metric_ar_up);
	}
}