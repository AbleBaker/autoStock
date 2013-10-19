/**
 * 
 */
package com.autoStock.signal.signalMetrics;

import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions;
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
