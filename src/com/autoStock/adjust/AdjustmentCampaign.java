package com.autoStock.adjust;

import java.util.ArrayList;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class AdjustmentCampaign {
	protected ArrayList<IterableBase> listOfIterableBase = new ArrayList<IterableBase>();
	protected ArrayList<AdjustmentBase> listOfAdjustmentBase = new ArrayList<AdjustmentBase>();
	private Permutation permutation = new Permutation(listOfIterableBase);
	public boolean isRebasing;
	
	public static enum AdjustmentType {
		signal_metric_long_entry,
		signal_metric_long_exit,
		signal_metric_short_entry,
		signal_metric_short_exit,
	}
	
	protected abstract void initializeAdjustmentCampaign();
	
	public void initialize(){
		initializeAdjustmentCampaign();
		for (AdjustmentBase adjustmentBase : listOfAdjustmentBase){
			listOfIterableBase.add(adjustmentBase.getIterableBase());
		}
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
	
	public boolean rebaseRequired(){
		for (IterableBase iterableBase : listOfIterableBase){
			if (iterableBase.rebaseRequired){
				return true;
			}
		}
		
		return false;
	}
	
	public void rebased(){
		for (IterableBase iterableBase : listOfIterableBase){
			iterableBase.rebaseRequired = false;
		}
	}
	
	public boolean hasMore(){
		return !permutation.allDone();
	}
	
	public double getPercentComplete(){
		return 0;
	}
	
	public void applyValues(){
		Co.println("--> Apply values");
		for (AdjustmentBase adjustmentBase : listOfAdjustmentBase){
//			Co.println("--> Applied: " + adjustmentBase.getClass().getSimpleName() + ", " + adjustmentBase.description);
//			Co.println("--> Check: " + ((AdjustmentOfBasicInteger)adjustmentBase).getValue());
			adjustmentBase.applyValue();
		}
	}
	
	public ArrayList<AdjustmentBase> getListOfAdjustmentBase(){
		return listOfAdjustmentBase;
	}
}
