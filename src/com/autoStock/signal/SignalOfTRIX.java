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
public class SignalOfTRIX extends SignalBase {
	
	public SignalOfTRIX(SignalParameters signalParameters){
		super(SignalMetricType.metric_trix, signalParameters);
	}
}
