/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfTRIX extends SignalBase {
	
	public SignalOfTRIX(){
		super(SignalMetricType.metric_trix);
	}
}
