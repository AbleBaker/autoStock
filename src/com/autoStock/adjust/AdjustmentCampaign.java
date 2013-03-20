package com.autoStock.adjust;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.MainBacktest;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalDefinitions;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentCampaign {
	private static AdjustmentCampaign instance = new AdjustmentCampaign();
	private ArrayList<IterableBase> listOfIterableBase = new ArrayList<IterableBase>();
	private ArrayList<AdjustmentBase> listOfAdjustmentBase = new ArrayList<AdjustmentBase>();
	private Permutation permutation = new Permutation(listOfIterableBase);
	
	public static enum AdjustmentType {
		signal_metric_long_entry,
		signal_metric_long_exit,
		signal_metric_short_entry,
		signal_metric_short_exit,
	}
	
	private AdjustmentCampaign(){
		initializeAdjustmentCampaign();
		
		for (AdjustmentBase adjustmentBase : listOfAdjustmentBase){
			listOfIterableBase.add(adjustmentBase.getIterableBase());
		}
	}
	
	public static AdjustmentCampaign getInstance(){
		return instance;
	}
	
	public void initializeAdjustmentCampaign(){
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_cci, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-50, 0, 2)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_cci, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(0, 50, 2)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_rsi, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-4, 40, 2)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_rsi, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(-40, 4, 2)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_di, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-10, 50, 2)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_di, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(-50, 10, 2)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_trix, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-4, 40, 2)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_trix, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(-40, 4, 2)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_macd, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-4, 40, 4)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_macd, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(-40, 4, 4)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_roc, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-4, 40, 1)));
//		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_roc, AdjustmentType.signal_metric_long_exit, new IterableOfInteger(-40, 4, 1)));
//		
//		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("MACD EMA", IndicatorOfMACD.immutableIntegerForEma, new IterableOfInteger(3, 30, 1)));
//		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("MACD long", IndicatorOfMACD.immutableIntegerForLong, new IterableOfInteger(10, 30, 1)));
//		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("MACD short", IndicatorOfMACD.immutableIntegerForShort, new IterableOfInteger(3, 30, 1)));
//		
////	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger(StrategyOptionManager.getInstance().getDefaultStrategyOptions().maxStopLossValue, new IterableOfInteger(-50, 0, 5)));
////	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger(StrategyOptionManager.getInstance().getDefaultStrategyOptions().intervalForReentryMins, new IterableOfInteger(1, 10, 1)));
////	listOfAdjustmentBase.add(new AdjustmentOfBasicInteger(StrategyOptionManager.getInstance().getDefaultStrategyOptions().maxReenterTimes, new IterableOfInteger(1, 3, 1)));
////	listOfAdjustmentBase.add(new AdjustmentOfBasicDouble(StrategyOptionManager.getInstance().getDefaultStrategyOptions().minReentryPercentGain, new IterableOfDouble(0.1, 0.5, 0.1)));
////	
//		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger(SignalControl.periodLengthStart, new IterableOfInteger(10, 30, 2)));
//		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("SignalControl.periodLengthMiddle", SignalControl.periodLengthMiddle, new IterableOfInteger(10, 40, 2)));
		
		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("CCI Period", IndicatorGroup.immutableIntegerForCCI, new IterableOfInteger(10, 60, 2)));
//		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger("DI Period", IndicatorGroup.immutableIntegerForDI, new IterableOfInteger(10, 60, 2)));
		
		listOfAdjustmentBase.add(new AdjustmentOfEnum("CCI Guage Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.values()[0]), SignalDefinitions.SignalMetricType.metric_cci.arrayOfSignalGuageForLongEntry[0].immutableEnumForSignalGuageType));
		listOfAdjustmentBase.add(new AdjustmentOfEnum("CCI Guage Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.values()[0]), SignalDefinitions.SignalMetricType.metric_cci.arrayOfSignalGuageForLongExit[0].immutableEnumForSignalGuageType));
	}
	
	public boolean runAdjustment(){
		if (permutation.allDone()){
			return false;
		}else{
			permutation.masterIterate();
			permutation.printIterableSet();
			applyValues();
			return true;
		}
	}
	
	public boolean hasMore(){
		return !permutation.allDone();
	}
	
	public double getPercentComplete(){
		return 0;
	}
	
	public void applyValues(){
		for (AdjustmentBase adjustmentBase : listOfAdjustmentBase){
			adjustmentBase.applyValue();
		}
	}
	
	public ArrayList<AdjustmentBase> getListOfAdjustmentBase(){
		return listOfAdjustmentBase;
	}

	public ArrayList<AdjustmentOfPortable> getListOfPortableAdjustment(){
		ArrayList<AdjustmentOfPortable> listOfPortable = new ArrayList<AdjustmentOfPortable>();
		
		for (AdjustmentBase adjustmentBase : listOfAdjustmentBase){
			AdjustmentOfPortable adjustmentOfPortable = new AdjustmentOfPortable(adjustmentBase.iterableBase.currentIndex);
			listOfPortable.add(adjustmentOfPortable);
		}
		
		return listOfPortable;
	}

	public void setAdjustmentValuesFromIterationList(ArrayList<AdjustmentOfPortable> listOfAdjustmentOfPortable) {
		if (listOfAdjustmentOfPortable.size() != listOfAdjustmentBase.size()){
			throw new IllegalStateException("Size doesn't match: " + listOfAdjustmentOfPortable.size() + ", " + listOfAdjustmentBase.size());
		}
		
		Co.println("--> Size is: " + listOfAdjustmentOfPortable.size());
		
		for (int i=0; i < listOfAdjustmentBase.size(); i++){
			AdjustmentBase adjustmentBaseLocal = listOfAdjustmentBase.get(i);
			AdjustmentOfPortable adjustmentOfPortable = listOfAdjustmentOfPortable.get(i);
			
			adjustmentBaseLocal.iterableBase.overrideAndSetCurrentIndex(adjustmentOfPortable.iterationIndex);
			adjustmentBaseLocal.applyValue();
		}
	}
}
