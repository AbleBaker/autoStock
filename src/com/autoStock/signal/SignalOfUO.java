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
public class SignalOfUO extends SignalBase {
	public SignalOfUO(SignalParameters signalParameters){
		super(SignalMetricType.metric_uo, signalParameters);
	}
}
