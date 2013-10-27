/**
 * 
 */
package com.autoStock.signal.signalMetrics;

import com.autoStock.Co;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfSAR extends SignalBase {
	public SignalOfSAR(SignalParameters signalParameters){
		super(SignalMetricType.metric_sar, signalParameters);
	}
	
	public void setInput(double value, double priceClose){
		Co.println("--> Set value: " + value + ", " + priceClose);
		super.setInput(priceClose);
	}
	
	@Override
	public void setInput(double value) {
		throw new IllegalAccessError("Use the other signature");
	}
}
