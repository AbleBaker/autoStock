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
	
	public static enum SignalTrend {
		type_trend_up,
		type_trend_down,
		type_trend_flat,
		type_none,
	}
	
	public static enum SignalPoint {
		long_entry,
		long_exit,
		short_entry,
		short_exit,
		none,
		; 
		
		public int occurences;
	}
	
	public enum SignalMetricType {
		metric_ppc(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) ((input - 1) * 2500);}},
				48, 44, 0, 0),
		metric_di(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input * 3);}},
				5, -24, 0, 0),
		metric_cci(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input / 4);}},
				42, -23, 0, 0),
		metric_macd(
			new CalculateInterface(){@Override public int calculate(double input){return (int) (input * 2000);}},
				0, 0, 0, 0),
		metric_rsi(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input / 2);}},
				36, -10, 0, 0),
		metric_trix(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input * 1000);}},
				33, -20, 0, 0),
		metric_storsi(null,0,0,0,0),
		;
		
		CalculateInterface calculateInterface;
		public int pointToSignalLongEntry = 0;
		public int pointToSignalLongExit = 0;
		public int pointToSignalShortEntry = 0;
		public int pointToSignalShortExit = 0;
		
		private SignalMetricType(CalculateInterface calculateInterface, int pointToSignalLongEntry, int pointToSignalLongExit, int pointToSignalShortEntry, int pointToSignalShortExit){
			this.calculateInterface = calculateInterface;
			this.pointToSignalLongEntry = pointToSignalLongEntry;
			this.pointToSignalLongExit = pointToSignalLongExit;
			this.pointToSignalShortEntry = pointToSignalShortEntry;
			this.pointToSignalShortExit = pointToSignalShortExit;
		}

		public int getSignalStrength(double input) {
			return this.calculateInterface.calculate(input);
		} 
	}
	
	public static SignalTrend getSignalType(Signal signal){
		if (signal.getCombinedSignal().strength > signal.getCombinedSignal().longEntry){
			return SignalTrend.type_trend_up;
		}else if (signal.getCombinedSignal().strength < signal.getCombinedSignal().shortEntry){
			return SignalTrend.type_trend_down;
		}else{
			return SignalTrend.type_trend_flat;
		}
	}
}
