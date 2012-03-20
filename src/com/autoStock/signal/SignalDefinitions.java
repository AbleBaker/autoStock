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
		type_buy,
		type_sell,
		type_hold,
		type_short,
		type_none,
	}
	
	public static enum SignalTypeMetric {
		metric_ppc,
		metric_adx,
		metric_cci,
		metric_macd,
	}
	
	public static double getSignalWeight(SignalTypeMetric signalTypeMetric){
		if (signalTypeMetric == SignalTypeMetric.metric_ppc){return SignalControl.weightForPPC;}
		else if (signalTypeMetric == SignalTypeMetric.metric_adx){return SignalControl.weightForADX;}
		else if (signalTypeMetric == SignalTypeMetric.metric_cci){return SignalControl.weightForCCI;}
		else if (signalTypeMetric == SignalTypeMetric.metric_macd){return SignalControl.weightForMACD;}
		throw new UnsupportedOperationException();
	}
}
