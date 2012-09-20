/**
 * 
 */
package com.autoStock.signal;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalDefinitions {
	public enum SignalSource{
		from_algorithm,
		from_market_trend,
		from_news,
		from_manual,
	}
	
	public enum SignalPoint {
		long_entry,
		long_exit,
		short_entry,
		short_exit,
		no_change,
		undefined,
		none,
		; 
		
		public int occurences;
		public SignalMetricType signalMetricType = SignalMetricType.none;
	}
	
	public enum SignalMetricType {
		metric_ppc(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) ((input - 1) * 3000);}},
				48, 44, -100, -100),
		metric_di(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input * 2);}},
				37, -26, -100, -100),
		metric_cci(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input / 4);}},
				48, 12, -100, -100),
		metric_macd(
			new CalculateInterface(){@Override public int calculate(double input){return (int) (input * 1000);}},
				20, -36, -100, -100),
		metric_rsi(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) Math.pow(input / 2, 1.20) - 50;}},
				15, -12, -100, -100),
		metric_trix(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input * 600);}},
				45, 20, 0, 0),
		metric_roc(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input * 30);}},
				35, -20, 0, 0),
		metric_mfi(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (Math.pow(input, 1.2) * 0.30) - 25;}},
				40, -24, 0, 0),
		metric_willr(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input + 30 * 1.0);}},
				30, -40, 0, 0),				
				
		metric_storsi(null,0,0,0,0),
		
		none,
		no_change,
		;
		
		CalculateInterface calculateInterface;
		public volatile int pointToSignalLongEntry = 0;
		public volatile int pointToSignalLongExit = 0;
		public volatile int pointToSignalShortEntry = 0;
		public volatile int pointToSignalShortExit = 0;
		
		private SignalMetricType(){
			
		}
		
		private SignalMetricType(CalculateInterface calculateInterface, int pointToSignalLongEntry, int pointToSignalLongExit, int pointToSignalShortEntry, int pointToSignalShortExit){
			this.calculateInterface = calculateInterface;
			this.pointToSignalLongEntry = pointToSignalLongEntry;
			this.pointToSignalLongExit = pointToSignalLongExit;
			this.pointToSignalShortEntry = pointToSignalShortEntry;
			this.pointToSignalShortExit = pointToSignalShortExit;
		}

		public synchronized int getSignalStrength(double input) {
			return this.calculateInterface.calculate(input);
		} 
	}
}
