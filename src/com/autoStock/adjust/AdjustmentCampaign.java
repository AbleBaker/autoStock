package com.autoStock.adjust;

import com.autoStock.Co;
import com.autoStock.adjust.Permutation.Iteration;
import com.autoStock.signal.SignalControl;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentCampaign {
	private static AdjustmentCampaign instance = new AdjustmentCampaign();
	private int adjustmentRun = 0;
	private Permutation permutation = new Permutation();
	
	public AdjustmentCampaign(){
		permutation.addIteration(new Iteration(
			AdjustmentDefinitions.algo_signal_long_entry.startValue, 
			AdjustmentDefinitions.algo_signal_long_entry.endValue, 
			AdjustmentDefinitions.algo_signal_long_entry));
		
		permutation.addIteration(new Iteration(
			AdjustmentDefinitions.algo_signal_long_exit.startValue,
			AdjustmentDefinitions.algo_signal_long_exit.endValue, 
			AdjustmentDefinitions.algo_signal_long_exit));
		
		permutation.addIteration(new Iteration(
			AdjustmentDefinitions.algo_signal_short_entry.startValue, 
			AdjustmentDefinitions.algo_signal_short_entry.endValue, 
			AdjustmentDefinitions.algo_signal_short_entry));
		
		permutation.addIteration(new Iteration(
			AdjustmentDefinitions.algo_signal_short_exit.startValue,
			AdjustmentDefinitions.algo_signal_short_exit.endValue, 
			AdjustmentDefinitions.algo_signal_short_exit));
	
		permutation.prepare();
	}
	
	public static AdjustmentCampaign getInstance(){
		return instance;
	}
	
	public enum AdjustmentDefinitions {
		algo_signal_long_entry(-10, 20),
		algo_signal_long_exit(-10, 20),
		algo_signal_short_entry(-20, 10),
		algo_signal_short_exit(-20, 10),
//		signal_cci_average(0, 10),
//		signal_di_average(0, SignalControl.periodWindow),
//		analysis_macd_fast(1, 15),
//		analysis_macd_slow(1, 30),
//		analysis_macd_signal(1, 30),
		;
		
		public int startValue;
		public int endValue;
		public int currentValue;
		
		private AdjustmentDefinitions(int start, int end) {
			this.startValue = start;
			this.endValue = end;
			this.currentValue = start;
		}
	}

	public boolean runAdjustment(){
		boolean ranPermutation = permutation.iterate();
		
		setAdjustmentValues();
		
		return ranPermutation;
	}
	
	private void setAdjustmentValues(){		
		for (Iteration iteration : permutation.getListOfIterations()){
			AdjustmentDefinitions adjustment = (AdjustmentDefinitions) iteration.getRequest();
			
			if (adjustment == AdjustmentDefinitions.algo_signal_long_entry){
				SignalControl.pointToSignalLongEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.pointToSignalLongEntry);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_long_exit){
				SignalControl.pointToSignalLongExit = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.pointToSignalLongExit);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_short_entry){
				SignalControl.pointToSignalShortEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.pointToSignalShortEntry);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_short_exit){
				SignalControl.pointToSignalShortExit = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.pointToSignalShortExit);
			}
			
			else {
				throw new UnsupportedOperationException("Undefined condition");
			}
		}
	}
	
	public int getAdjustmentValueOfInt(AdjustmentDefinitions adjustment){
		return adjustment.currentValue;
	}
}
