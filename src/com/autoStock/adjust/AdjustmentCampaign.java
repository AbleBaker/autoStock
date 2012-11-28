package com.autoStock.adjust;

import java.util.ArrayList;

import com.autoStock.adjust.AdjustmentInterfaces.AdjustmentInterfaceForInteger;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.strategy.StrategyOptionManager;

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
		setupAdjustmentForComplete();
		
		for (AdjustmentBase adjustmentBase : listOfAdjustmentBase){
			listOfIterableBase.add(adjustmentBase.getIterableBase());
		}
	}
	
	public void setupAdjustmentForComplete(){
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetric(SignalMetricType.metric_di, AdjustmentType.signal_metric_long_entry, new IterableOfInteger(-50, 50, 1)));	
		
		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger(StrategyOptionManager.getInstance().getDefaultStrategyOptions().intervalForReentryMins, new AdjustmentInterfaceForInteger(){
		@Override public void setValue(Integer integer) {StrategyOptionManager.getInstance().getDefaultStrategyOptions().intervalForReentryMins = integer;}
		}, new IterableOfInteger(-50, 50, 1)));
		
		
		
	}
	
	public static AdjustmentCampaign getInstance(){
		return instance;
	}
	
	public boolean runAdjustment(){
		return !permutation.allDone();
	}
	
	public double getPercentComplete(){
		return 0;
	}
	
	public ArrayList<AdjustmentBase> getListOfAdjustmentBase(){
		return listOfAdjustmentBase;
	}
}
