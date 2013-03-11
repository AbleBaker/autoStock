/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfROC extends SignalBase {
	
	public SignalOfROC(){
		super(SignalMetricType.metric_roc);
	}
}
