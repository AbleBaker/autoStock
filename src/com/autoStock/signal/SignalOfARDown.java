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
public class SignalOfARDown extends SignalBase {
	
	public SignalOfARDown(SignalParameters signalParameters){
		super(SignalMetricType.metric_ar_down, signalParameters);
	}
}
