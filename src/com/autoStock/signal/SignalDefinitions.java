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
		metric_ppc(new CalculateInterface(){@Override public int calculate(double input) {return (int) ((input - 1) * 10000);}}),
		metric_di(new CalculateInterface(){@Override public int calculate(double input) {return (int) (input * 2);}}),
		metric_cci(new CalculateInterface(){@Override public int calculate(double input) {return (int) (input / 4);}}),
		metric_macd(new CalculateInterface(){@Override public int calculate(double input){return (int) (input * 5000);}}),
		metric_rsi(new CalculateInterface(){@Override public int calculate(double input) {return (int) (input / 4);}}),
		metric_trix(new CalculateInterface(){@Override public int calculate(double input) {return (int) (input * 1000);}}),
		metric_storsi(null),
		;
		
		CalculateInterface calculateInterface;
		int pointToSignalLongEntry = 0;
		int pointToSignalLongExit = 0;
		int pointToSignalShortEntry = 0;
		int pointToSignalShortExit = 0;
		
		private SignalTypeMetric(CalculateInterface calculateInterface) {
			this.calculateInterface = calculateInterface;
		}

		public int getSignalStrength(double input) {
			return this.calculateInterface.calculate(input);
		} 
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
