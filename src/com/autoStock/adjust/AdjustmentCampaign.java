package com.autoStock.adjust;

import com.autoStock.Co;
import com.autoStock.adjust.Permutation.Iteration;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentCampaign {
	private static AdjustmentCampaign instance = new AdjustmentCampaign();
	private int adjustmentRun = 0;
	private Permutation permutation = new Permutation();
	
	public AdjustmentCampaign(){
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_ppc_long_entry, SignalMetricType.metric_ppc));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_ppc_long_exit, SignalMetricType.metric_ppc));
//		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_di_long_entry, SignalMetricType.metric_di));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_di_long_exit, SignalMetricType.metric_di));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_cci_long_entry, SignalMetricType.metric_cci));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_cci_long_exit, SignalMetricType.metric_cci));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_trix_long_entry, SignalMetricType.metric_trix));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_trix_long_exit, SignalMetricType.metric_trix));
		
		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_rsi_long_entry, SignalMetricType.metric_rsi));
		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_rsi_long_exit, SignalMetricType.metric_rsi));
		

		
		//
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_short_entry.startValue, 
//			AdjustmentDefinitions.algo_signal_short_entry.endValue, 
//			AdjustmentDefinitions.algo_signal_short_entry));
//		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_short_exit.startValue,
//			AdjustmentDefinitions.algo_signal_short_exit.endValue, 
//			AdjustmentDefinitions.algo_signal_short_exit));
//		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_period_length.startValue,
//			AdjustmentDefinitions.algo_signal_period_length.endValue,
//			AdjustmentDefinitions.algo_signal_period_length));
//		
//		permutation.addIteration(new Iteration(
//			AdjustmentDefinitions.algo_signal_period_window.startValue,
//			AdjustmentDefinitions.algo_signal_period_window.endValue,
//			AdjustmentDefinitions.algo_signal_period_window));
//		
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
		
		algo_signal_metric_ppc_long_entry(-25,50),
		algo_signal_metric_ppc_long_exit(-25,50),
		algo_signal_metric_ppc_short_entry(-25,25),
		algo_signal_metric_ppc_short_exit(-25,25),
		
		algo_signal_metric_di_long_entry(-50,50),
		algo_signal_metric_di_long_exit(-50,50),
		algo_signal_metric_di_short_entry(-50,50),
		algo_signal_metric_di_short_exit(-50,50),
		
		algo_signal_metric_cci_long_entry(-25,50),
		algo_signal_metric_cci_long_exit(-25,50),
		algo_signal_metric_cci_short_entry(-50,50),
		algo_signal_metric_cci_short_exit(-50,50),
		
		algo_signal_metric_rsi_long_entry(-25,50),
		algo_signal_metric_rsi_long_exit(-25,50),
		algo_signal_metric_rsi_short_entry(-50,50),
		algo_signal_metric_rsi_short_exit(-50,50),
		
		algo_signal_metric_trix_long_entry(-25,50),
		algo_signal_metric_trix_long_exit(-25,50),
		algo_signal_metric_trix_short_entry(-50,50),
		algo_signal_metric_trix_short_exit(-50,50),
		
		algo_signal_long_entry(-50,50),
		algo_signal_long_exit(-100,50),
		algo_signal_short_entry(-50,50),
		algo_signal_short_exit(-50,50),
		
		algo_signal_long_entry_di(-50,50),
		algo_signal_long_exit_di(-100,50),
		algo_signal_short_entry_di(-50,50),
		algo_signal_short_exit_di(-50,50),
		
		algo_signal_long_entry_cci(-50,50),
		algo_signal_long_exit_cci(-100,50),
		algo_signal_short_entry_cci(-50,50),
		algo_signal_short_exit_cci(-50,50),
		
		algo_signal_long_entry_ppc(-50,50),
		algo_signal_long_exit_ppc(-100,50),
		algo_signal_short_entry_ppc(-50,50),
		algo_signal_short_exit_ppc(-50,50),
		
		algo_signal_long_entry_macd(-50,50),
		algo_signal_long_exit_macd(-100,50),
		algo_signal_short_entry_macd(-50,50),
		algo_signal_short_exit_macd(-50,50),
		
		algo_signal_long_entry_trix(-50,50),
		algo_signal_long_exit_trix(-100,50),
		algo_signal_short_entry_trix(-50,50),
		algo_signal_short_exit_trix(-50,50),
		
		algo_signal_period_length(15, 60),
		
		algo_signal_period_average_ppc(0,8),
		algo_signal_period_average_di(0,8),
		algo_signal_period_average_cci(0,8),
		algo_signal_period_average_macd(0,8),
		algo_signal_period_average_trix(0,8),
		
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
			
			if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_ppc_long_entry){
				iteration.signalTypeMetric.pointToSignalLongEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_ppc_long_exit){
				iteration.signalTypeMetric.pointToSignalLongExit = (int) permutation.getIteration(adjustment).getCurrentValue(); 
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_ppc_short_entry){
				iteration.signalTypeMetric.pointToSignalShortEntry = (int) permutation.getIteration(adjustment).getCurrentValue(); 
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_ppc_short_exit){
				iteration.signalTypeMetric.pointToSignalShortExit = (int) permutation.getIteration(adjustment).getCurrentValue(); 
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_cci_long_entry){
				iteration.signalTypeMetric.pointToSignalLongEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_cci_long_exit){
				iteration.signalTypeMetric.pointToSignalLongExit = (int) permutation.getIteration(adjustment).getCurrentValue(); 
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_cci_short_entry){
				iteration.signalTypeMetric.pointToSignalShortEntry = (int) permutation.getIteration(adjustment).getCurrentValue(); 
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_cci_short_exit){
				iteration.signalTypeMetric.pointToSignalShortExit = (int) permutation.getIteration(adjustment).getCurrentValue(); 
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_rsi_long_entry){
				iteration.signalTypeMetric.pointToSignalLongEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_rsi_long_exit){
				iteration.signalTypeMetric.pointToSignalLongExit = (int) permutation.getIteration(adjustment).getCurrentValue(); 
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_trix_long_entry){
				iteration.signalTypeMetric.pointToSignalLongEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_trix_long_exit){
				iteration.signalTypeMetric.pointToSignalLongExit = (int) permutation.getIteration(adjustment).getCurrentValue(); 
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_di_long_entry){
				iteration.signalTypeMetric.pointToSignalLongEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment == AdjustmentDefinitions.algo_signal_metric_di_long_exit){
				iteration.signalTypeMetric.pointToSignalLongExit = (int) permutation.getIteration(adjustment).getCurrentValue(); 
			}
			
//			//Entry & Exit
//			else if (adjustment == AdjustmentDefinitions.algo_signal_long_entry){
//				SignalControl.pointToSignalLongEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
//				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.pointToSignalLongEntry);
//			}
//			
//			else if (adjustment == AdjustmentDefinitions.algo_signal_long_exit){
//				SignalControl.pointToSignalLongExit = (int) permutation.getIteration(adjustment).getCurrentValue();
//				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.pointToSignalLongExit);
//			}
//			
//			else if (adjustment == AdjustmentDefinitions.algo_signal_short_entry){
//				SignalControl.pointToSignalShortEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
//				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.pointToSignalShortEntry);
//			}
//			
//			else if (adjustment == AdjustmentDefinitions.algo_signal_short_exit){
//				SignalControl.pointToSignalShortExit = (int) permutation.getIteration(adjustment).getCurrentValue();
//				Co.println("******** Changed " + adjustment.name() + " to " + SignalControl.pointToSignalShortExit);
//			}
			
			//Period lengths
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_length){
				SignalControl.periodLength = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			//Analysis averages
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_average_ppc){
				SignalControl.periodAverageForPPC = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_average_di){
				SignalControl.periodAverageForDI = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_average_cci){
				SignalControl.periodAverageForCCI = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_average_macd){
				SignalControl.periodAverageForMACD = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (adjustment == AdjustmentDefinitions.algo_signal_period_average_trix){
				SignalControl.periodAverageForTRIX = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
						
			else {
				throw new UnsupportedOperationException("Undefined condition: " + adjustment.name());
			}
			
			Co.println("******** Changed " + adjustment.name() + " to " +  (int) permutation.getIteration(adjustment).getCurrentValue());

		}
		
		Co.println("\n");
	}
	
	public int getAdjustmentValueOfInt(AdjustmentDefinitions adjustment){
		return adjustment.currentValue;
	}
	
	public double getPercentComplete(){
		return permutation.getPercentComplete();
	}
}
