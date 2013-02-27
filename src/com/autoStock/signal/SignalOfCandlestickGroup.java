package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCandlestickGroup extends SignalBase {
	public SignalOfCandlestickGroup() {
		super(SignalMetricType.metric_candlestick_group);
	}

	@Override
	public SignalMetric getSignal() {
		return new SignalMetric(0, signalMetricType);
	}
}
