/**
 * 
 */
package com.autoStock.signal;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalDefinitions {
	public static enum SignalSource{
		from_analysis,
		from_market_trend,
		from_news,
		from_manual,
	}
	
	public static enum SignalType {
		type_trend_up,
		type_trend_down,
		type_trend_flat,
		type_none,
	}
	
	public static enum SignalTypeMetric {
		metric_ppc,
		metric_adx,
		metric_cci,
		metric_macd,
		metric_storsi,
		metric_rsi,
	}
	
	public static double getSignalWeight(SignalTypeMetric signalTypeMetric){
		if (signalTypeMetric == SignalTypeMetric.metric_ppc){return SignalControl.weightForPPC;}
		else if (signalTypeMetric == SignalTypeMetric.metric_adx){return SignalControl.weightForADX;}
		else if (signalTypeMetric == SignalTypeMetric.metric_cci){return SignalControl.weightForCCI;}
		else if (signalTypeMetric == SignalTypeMetric.metric_macd){return SignalControl.weightForMACD;}
		throw new UnsupportedOperationException();
	}
}
