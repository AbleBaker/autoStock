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
		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_period_length.startValue,
//			AdjustmentDefinitions.algo_signal_period_length.endValue,
//			AdjustmentDefinitions.algo_signal_period_length));
//		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_period_window.startValue,
//			AdjustmentDefinitions.algo_signal_period_window.endValue,
//			AdjustmentDefinitions.algo_signal_period_window));
		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_period_average_ppc.startValue,
//			AdjustmentDefinitions.algo_signal_period_average_ppc.endValue,
//			AdjustmentDefinitions.algo_signal_period_average_ppc));
//		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_period_average_di.startValue,
//			AdjustmentDefinitions.algo_signal_period_average_di.endValue,
//			AdjustmentDefinitions.algo_signal_period_average_di));
//		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_period_average_cci.startValue,
//			AdjustmentDefinitions.algo_signal_period_average_cci.endValue,
//			AdjustmentDefinitions.algo_signal_period_average_cci));
//		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_period_average_macd.startValue,
//			AdjustmentDefinitions.algo_signal_period_average_macd.endValue,
//			AdjustmentDefinitions.algo_signal_period_average_macd));
//		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_period_average_trix.startValue,
//			AdjustmentDefinitions.algo_signal_period_average_trix.endValue,
//			AdjustmentDefinitions.algo_signal_period_average_trix));
//		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_weight_ppc.startValue,
//			AdjustmentDefinitions.algo_signal_weight_ppc.endValue,
//			AdjustmentDefinitions.algo_signal_weight_ppc));
//		
//		permutation.addIteration(new Iteration(
//				AdjustmentDefinitions.algo_signal_weight_di.startValue,
//				AdjustmentDefinitions.algo_signal_weight_di.endValue,
//				AdjustmentDefinitions.algo_signal_weight_di));
//		
//		permutation.addIteration(new Iteration(
//				AdjustmentDefinitions.algo_signal_weight_cci.startValue,
//				AdjustmentDefinitions.algo_signal_weight_cci.endValue,
//				AdjustmentDefinitions.algo_signal_weight_cci));
//		
//		permutation.addIteration(new Iteration(
//				AdjustmentDefinitions.algo_signal_weight_macd.startValue,
//				AdjustmentDefinitions.algo_signal_weight_macd.endValue,
//				AdjustmentDefinitions.algo_signal_weight_macd));
//		
//		permutation.addIteration(new Iteration(
//				AdjustmentDefinitions.algo_signal_weight_trix.startValue,
//				AdjustmentDefinitions.algo_signal_weight_trix.endValue,
//				AdjustmentDefinitions.algo_signal_weight_trix));
	
		permutation.prepare();
	}
	
	public static AdjustmentCampaign getInstance(){
		return instance;
	}
	
	public enum AdjustmentDefinitions {
		algo_signal_long_entry(0,20),
		algo_signal_long_exit(-15,20),
		algo_signal_short_entry(-15,0),
		algo_signal_short_exit(-15,0),
		
		algo_signal_period_length(15,45),
		algo_signal_period_window(15,30),
		
		algo_signal_period_average_ppc(0,4),
		algo_signal_period_average_di(0,4),
		algo_signal_period_average_cci(0,4),
		algo_signal_period_average_macd(0,4),
		algo_signal_period_average_trix(0,4),
		
		algo_signal_weight_ppc(0,4),
		algo_signal_weight_di(0,4),
		algo_signal_weight_cci(0,4),
		algo_signal_weight_macd(0,4),
		algo_signal_weight_trix(0,4),
		
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
			
			//Entry & Exit
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
			
			//Period and Window lengths
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_length){
				SignalControl.periodLength = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.periodLength);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_window){
				SignalControl.periodWindow = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.periodWindow);
			}
			
			//Analysis averages
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_average_ppc){
				SignalControl.periodAverageForPPC = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.periodAverageForPPC);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_average_di){
				SignalControl.periodAverageForDI = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.periodAverageForDI);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_average_cci){
				SignalControl.periodAverageForCCI = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.periodAverageForCCI);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_average_macd){
				SignalControl.periodAverageForMACD = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.periodAverageForMACD);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_average_trix){
				SignalControl.periodAverageForTRIX = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.periodAverageForTRIX);
			}
			
			//Weights
			else if (adjustment == AdjustmentDefinitions.algo_signal_weight_ppc){
				SignalControl.weightForPPC = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.weightForPPC);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_weight_di){
				SignalControl.weightForDI = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.weightForDI);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_weight_cci){
				SignalControl.weightForCCI = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.weightForCCI);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_weight_macd){
				SignalControl.weightForMACD = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.weightForMACD);
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_weight_trix){
				SignalControl.weightForTRIX = (int) permutation.getIteration(adjustment).getCurrentValue();
				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.weightForTRIX);
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
