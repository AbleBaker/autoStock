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
public class SignalOfRSI extends SignalBase {

	public SignalOfRSI(SignalParameters signalParameters){
		super(SignalMetricType.metric_rsi, signalParameters);
	}
}
