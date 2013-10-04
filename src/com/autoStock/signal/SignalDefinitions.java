/**
 * 
 */
package com.autoStock.signal;
import java.util.ArrayList;

import com.autoStock.guage.SignalGuage;
import com.autoStock.tools.MathTools;
import com.autoStock.types.basic.MutableEnum;
import com.autoStock.types.basic.MutableInteger;
import com.google.gson.internal.Pair;
import com.rits.cloning.Cloner;

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
	
	public static enum SignalGuageType {
//		guage_peak,
//		guage_trough,
		guage_threshold_met,
		guage_threshold_left,
		;
	}
	
	public static enum SignalBounds {
		bounds_upper,
		bounds_lower,
	}
	
	public static enum SignalCoherence {
		//cusp
		//fringe
		//peak
		//trough
		//steady
		//tapered
		//crossover
	}
	
	public static enum SignalMetricType {
		metric_adx,
		metric_cci,
		metric_rsi,
		metric_di,
		metric_ppc,
		metric_macd,
		metric_trix,
		metric_roc,
		metric_mfi,
		metric_willr,
		metric_storsi,
		metric_uo,
		metric_ar_up,
		metric_ar_down,
		
		metric_candlestick_group,
		metric_encog,
		
		none,
		mixed,
		no_change,
	}
	
	public static abstract class SignalParameters {
		public NormalizeInterface normalizeInterface;
		public MutableInteger periodLength;
		public MutableInteger maxSignalAverage;
		public SignalGuage[] arrayOfSignalGuageForLongEntry;
		public SignalGuage[] arrayOfSignalGuageForLongExit;
		public SignalGuage[] arrayOfSignalGuageForShortEntry;
		public SignalGuage[] arrayOfSignalGuageForShortExit;
		
		public SignalParameters(NormalizeInterface normalizeInterface, MutableInteger periodLength, MutableInteger maxSignalAverage, SignalGuage[] arrayOfSignalGuageForLongEntry, SignalGuage[] arrayOfSignalGuageForLongExit, SignalGuage[] arrayOfSignalGuageForShortEntry, SignalGuage[] arrayOfSignalGuageForShortExit) {
			this.normalizeInterface = normalizeInterface;
			this.periodLength = periodLength;
			this.maxSignalAverage = maxSignalAverage;
			this.arrayOfSignalGuageForLongEntry = arrayOfSignalGuageForLongEntry;
			this.arrayOfSignalGuageForLongExit = arrayOfSignalGuageForLongExit;
			this.arrayOfSignalGuageForShortEntry = arrayOfSignalGuageForShortEntry;
			this.arrayOfSignalGuageForShortExit = arrayOfSignalGuageForShortExit;
		}
		
		public SignalParameters(){} //For Gson
		
		public int getNormalizedValue(double input){
			return this.normalizeInterface.normalize(input);
		}
		
		public ArrayList<Pair<SignalPointType, SignalGuage[]>> getGuages(){
			ArrayList<Pair<SignalPointType, SignalGuage[]>> listOfGuages = new ArrayList<Pair<SignalPointType, SignalGuage[]>>();
			
			listOfGuages.add(new Pair<SignalPointType, SignalGuage[]>(SignalPointType.long_entry, arrayOfSignalGuageForLongEntry));
			listOfGuages.add(new Pair<SignalPointType, SignalGuage[]>(SignalPointType.long_exit, arrayOfSignalGuageForLongExit));
			listOfGuages.add(new Pair<SignalPointType, SignalGuage[]>(SignalPointType.short_entry, arrayOfSignalGuageForShortEntry));
			listOfGuages.add(new Pair<SignalPointType, SignalGuage[]>(SignalPointType.short_exit, arrayOfSignalGuageForShortExit));
			
			return listOfGuages;
		}
		
		public SignalParameters copy(){
//			return new Gson().fromJson(new Gson().toJson(this), this.getClass());
			return new Cloner().deepClone(this);
		}
	}
	
	public static class SignalParametersForADX extends SignalParameters {
		public SignalParametersForADX(){
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (MathTools.pow(input - 10, 0.8));}}, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, 48)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -44)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForPPC extends SignalParameters {
		public SignalParametersForPPC() {
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) ((input - 1) * 3000);}}, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, 48)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -44)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForDI extends SignalParameters {
		public SignalParametersForDI(){
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) input * 1;}},
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForCCI extends SignalParameters {
		public SignalParametersForCCI() {
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input / 6);}}, 
			new MutableInteger(30), new MutableInteger(10),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_left), SignalBounds.bounds_lower, -30)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_left), SignalBounds.bounds_upper, 0)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_left), SignalBounds.bounds_upper, 18)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_left), SignalBounds.bounds_lower, -30)});
		}
	}
	
	public static class SignalParametersForMACD extends SignalParameters {
		public SignalParametersForMACD() {
			super(new NormalizeInterface(){@Override public int normalize(double input){return (int) (input * 800);}}, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -10)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, 20)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForRSI extends SignalParameters {
		public SignalParametersForRSI(){
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input - 45);}}, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -15)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, 30)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -15)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, 30)});
		}
	}
	
	public static class SignalParametersForTRIX extends SignalParameters {
		public SignalParametersForTRIX() {
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input * 600);}}, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, 48)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -44)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForROC extends SignalParameters {
		public SignalParametersForROC() {
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input * 25);}}, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, 11)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -18)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForMFI extends SignalParameters {
		public SignalParametersForMFI() {
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input * 1.0) - 50;}}, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, 20)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -15)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForWILLR extends SignalParameters {
		public SignalParametersForWILLR() {
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (input  + 50);}}, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, 30)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -40)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForSTORSI extends SignalParameters {
		public SignalParametersForSTORSI() {
			super(null, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, 48)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -44)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForUO extends SignalParameters {
		public SignalParametersForUO() {
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (MathTools.pow(input, 1) - 30);}}, 
			new MutableInteger(30), new MutableInteger(10),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, 48)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -44)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForARUp extends SignalParameters {
		public SignalParametersForARUp(){
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (MathTools.pow(input - 10, 0.8));}}, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, 48)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -44)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForARDown extends SignalParameters {
		public SignalParametersForARDown(){
			super(new NormalizeInterface(){@Override public int normalize(double input) {return (int) (MathTools.pow(input - 10, 0.8));}}, 
			new MutableInteger(30), new MutableInteger(1),
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, 48)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -44)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_lower, -100)},
			new SignalGuage[]{new SignalGuage(new MutableEnum<SignalGuageType>(SignalGuageType.guage_threshold_met), SignalBounds.bounds_upper, -100)});
		}
	}
	
	public static class SignalParametersForEncog extends SignalParameters {
		public SignalParametersForEncog(){
			super(null, null, null, null, null, null, null);
		}
	}
	
	public static class SignalParametersForCandlestickGroup extends SignalParameters {
		public SignalParametersForCandlestickGroup(){
			super(null, null, null, null, null, null, null);
		}
	}
	
//	public enum SignalMetricType {
//		
//		metric_encog,
//		metric_candlestick_group,
//		
//		none,
//		no_change,
//		mixed,
//		;
//		
//		NormalizeInterface normalizeInterface;
//		public ImmutableInteger periodLength;
//		public ImmutableInteger maxSignalAverage;
//		public SignalGuage[] arrayOfSignalGuageForLongEntry;
//		public SignalGuage[] arrayOfSignalGuageForLongExit;
//		public SignalGuage[] arrayOfSignalGuageForShortEntry;
//		public SignalGuage[] arrayOfSignalGuageForShortExit;
//		
//		private SignalMetricType(){}
//		
//		private SignalMetricType(NormalizeInterface normalizeInterface, ImmutableInteger periodLength, ImmutableInteger maxSignalAverage, SignalGuage[] arrayOfSignalGuageForLongEntry, SignalGuage[] arrayOfSignalGuageForLongExit, SignalGuage[] arrayOfSignalGuageForShortEntry, SignalGuage[] arrayOfSignalGuageForShortExit){
//			this.periodLength = periodLength;
//			this.normalizeInterface = normalizeInterface;
//			this.maxSignalAverage = maxSignalAverage;
//			this.arrayOfSignalGuageForLongEntry = arrayOfSignalGuageForLongEntry;
//			this.arrayOfSignalGuageForLongExit = arrayOfSignalGuageForLongExit;
//			this.arrayOfSignalGuageForShortEntry = arrayOfSignalGuageForShortEntry;
//			this.arrayOfSignalGuageForShortExit = arrayOfSignalGuageForShortExit;
//		}
//
//		public synchronized int getNormalizedValue(double input) {
//			return this.normalizeInterface.normalize(input);
//		}
//	}
}
