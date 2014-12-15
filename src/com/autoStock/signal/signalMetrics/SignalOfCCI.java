/**
 * 
 */
package com.autoStock.signal.signalMetrics;

import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCCI extends SignalBase {	
	public SignalOfCCI(SignalParameters signalParameters) {
		super(SignalMetricType.metric_cci, signalParameters);
	}
}
