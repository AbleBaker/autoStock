/**
 * 
 */
package com.autoStock.signal.signalMetrics;

import com.autoStock.indicator.results.ResultsBase;
import com.autoStock.indicator.results.ResultsDI;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.tools.ArrayTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfDI extends SignalBase {
	public SignalOfDI(SignalParameters signalParameters){
		super(SignalMetricType.metric_di, signalParameters);
	}

	@Override
	public void setInput(ResultsBase resultsBase) {
		ResultsDI results = (ResultsDI)resultsBase;
		setInput(ArrayTools.getLastElement(results.arrayOfDIPlus) - ArrayTools.getLastElement(results.arrayOfDIMinus)); 
	}
}
