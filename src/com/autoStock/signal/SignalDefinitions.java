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
		metric_ppc(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) ((input - 1) * 2500);}},
			1,2,3,4),
		metric_di(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input * 2);}},
			1,2,3,4),
		metric_cci(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input / 4);}},
			1,2,3,4),
		metric_macd(
			new CalculateInterface(){@Override public int calculate(double input){return (int) (input * 5000);}},
			1,2,3,4),
		metric_rsi(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input *5000 / 4);}},
			1,2,3,4),
		metric_trix(
			new CalculateInterface(){@Override public int calculate(double input) {return (int) (input * 1000);}},
			1,2,3,4),
		metric_storsi(null,0,0,0,0),
		;
		
		CalculateInterface calculateInterface;
		int pointToSignalLongEntry = 0;
		int pointToSignalLongExit = 0;
		int pointToSignalShortEntry = 0;
		int pointToSignalShortExit = 0;
		
		private SignalTypeMetric(CalculateInterface calculateInterface, int pointToSignalLongEntry, int pointToSignalLongExit, int pointToSignalShortEntry, int pointToSignalShortExit){
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
