/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfARDown extends SignalBase {
	
	public SignalOfARDown(){
		super(SignalMetricType.metric_ar_down);
	}
}
