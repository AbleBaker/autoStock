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
public class SignalOfMACD extends SignalBase {
	public SignalOfMACD(SignalParameters signalParameters){
		super(SignalMetricType.metric_macd, signalParameters);
	}
}
