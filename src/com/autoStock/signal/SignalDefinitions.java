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
		metric_di,
		metric_cci,
		metric_macd,
		metric_storsi,
		metric_rsi,
		metric_trix,
	}
	
	public static double getSignalWeight(SignalTypeMetric signalTypeMetric){
		if (signalTypeMetric == SignalTypeMetric.metric_ppc){return SignalControl.weightForPPC;}
		else if (signalTypeMetric == SignalTypeMetric.metric_di){return SignalControl.weightForDI;}
		else if (signalTypeMetric == SignalTypeMetric.metric_cci){return SignalControl.weightForCCI;}
		else if (signalTypeMetric == SignalTypeMetric.metric_macd){return SignalControl.weightForMACD;}
		else if (signalTypeMetric == SignalTypeMetric.metric_trix){return SignalControl.weightForTRIX;}
		throw new UnsupportedOperationException();
	}
	
	public static SignalType getSignalType(Signal signal){
		if (signal.getCombinedSignal() > SignalControl.pointToSignalLongEntry){
			return SignalType.type_trend_up;
		}else if (signal.getCombinedSignal() < SignalControl.pointToSignalShortEntry){
			return SignalType.type_trend_down;
		}else{
			return SignalType.type_trend_flat;
		}
	}
}
