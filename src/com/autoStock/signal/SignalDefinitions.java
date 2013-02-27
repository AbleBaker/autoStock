/**
 * 
 */
package com.autoStock.signal;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalDefinitions {
	public static enum SignalSource{
		from_algorithm,
		from_market_trend,
		from_news,
		from_manual
	}
	
	public static enum SignalPointType {
		long_entry,
		long_exit,
		short_entry,
		short_exit,
		no_change,
		none
	}
	
	public static enum SignalCoherence {
		//fringe
		//peak
		//trough
		//steady
		//tapered
		//crossover
	}
	
	public enum SignalMetricType {
		metric_adx(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (MathTools.pow(input - 10, 0.8));}},
				48, 44, -100, -100),
		metric_ppc(
			new NormalizeInterface(){@Override public int normalize(double input) {return (int) ((input - 1) * 3000);}},
				48, 44, -100, -100),
		metric_di(
			new NormalizeInterface(){@Override public int normalize(double input) {return (int) input * 1 - 10;}},
				30, -18, -100, -100),
		metric_cci(
			new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input / 6);}},
				-20, 20, -100, -100),
		metric_macd(
			new NormalizeInterface(){@Override public int normalize(double input){return (int) (input * 1000);}},
				-2, -26, -100, -100),
		metric_rsi(
			new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input - 55);}},
				24, -18, -100, -100),
		metric_trix(
			new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input * 700);}},
				6, 0, 0, 0),
		metric_roc(
			new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input * 25);}},
				11, -18, 0, 0),
		metric_mfi(
			new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input * 1.0) - 50;}},
				20, -15, 0, 0),
		metric_willr(
			new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input  + 50);}},
				30, -40, 0, 0),				
				
		metric_storsi(null,0,0,0,0),
		
		metric_candlestick_group,
		
		none,
		no_change,
		mixed,
		;
		
		NormalizeInterface normalizeInterface;
		public volatile int pointToSignalLongEntry = 0;
		public volatile int pointToSignalLongExit = 0;
		public volatile int pointToSignalShortEntry = 0;
		public volatile int pointToSignalShortExit = 0;
		
		private SignalMetricType(){}
		
		private SignalMetricType(NormalizeInterface normalizeInterface, int pointToSignalLongEntry, int pointToSignalLongExit, int pointToSignalShortEntry, int pointToSignalShortExit){
			this.normalizeInterface = normalizeInterface;
			this.pointToSignalLongEntry = pointToSignalLongEntry;
			this.pointToSignalLongExit = pointToSignalLongExit;
			this.pointToSignalShortEntry = pointToSignalShortEntry;
			this.pointToSignalShortExit = pointToSignalShortExit;
		}

		public synchronized int getNormalizedValue(double input) {
			return this.normalizeInterface.normalize(input);
		} 
	}
}
