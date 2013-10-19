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
public class SignalOfUO extends SignalBase {
	public SignalOfUO(SignalParameters signalParameters){
		super(SignalMetricType.metric_uo, signalParameters);
	}
}
