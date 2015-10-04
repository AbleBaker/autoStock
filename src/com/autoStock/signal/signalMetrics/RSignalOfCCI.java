/**
 * 
 */
package com.autoStock.signal.signalMetrics;

import com.autoStock.signal.RSignalBase;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;

/**
 * @author Kevin Kowalewski
 *
 */
public class RSignalOfCCI extends RSignalBase {	
	public RSignalOfCCI(SignalParameters signalParameters) {
		super(SignalMetricType.metric_cci, signalParameters);
	}
}
