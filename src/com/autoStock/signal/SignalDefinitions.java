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
		from_algorithm,
		from_market_trend,
		from_news,
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
	}
}
