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
	private Permutation permutation = new Permutation();
	
	public static AdjustmentCampaign getInstance(){
		return instance;
	}
	
	public AdjustmentCampaign(){
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_ppc_long_entry, SignalMetricType.metric_ppc));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_ppc_long_exit, SignalMetricType.metric_ppc));
//		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_di));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_di));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_cci));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_cci));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_trix));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_trix));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_macd));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_macd));
//		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_rsi));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_rsi));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_control_stop_loss, SignalMetricType.metric_none));
//		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_short_entry, SignalMetricType.metric_rsi));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_short_exit, SignalMetricType.metric_rsi));
		
		permutation.prepare();
	}
	
	public enum AdjustmentType{
		long_entry,
		long_exit,
		short_entry,
		short_exit,
		signal_control,
	}
	
	public enum AdjustmentDefinitions {		
		algo_signal_metric_long_entry(-50, 50, AdjustmentType.long_entry),
		algo_signal_metric_long_exit(-50, 50, AdjustmentType.long_exit),
		algo_signal_metric_short_entry(-50, 50, AdjustmentType.short_entry),
		algo_signal_metric_short_exit(-50, 50, AdjustmentType.short_exit),
//		
//		algo_signal_period_length(15, 60),
		
//		algo_signal_control_stop_loss(-50, 0, AdjustmentType.signal_control);
//		
//		algo_signal_period_average_ppc(0,8),
//		algo_signal_period_average_di(0,8),
//		algo_signal_period_average_cci(0,8),
//		algo_signal_period_average_macd(0,8),
//		algo_signal_period_average_trix(0,8),
//		
//		algo_signal_weight_ppc(0,4),
//		algo_signal_weight_di(0,4),
//		algo_signal_weight_cci(0,4),
//		algo_signal_weight_macd(0,4),
//		algo_signal_weight_trix(0,4),
		
		;
		
		public int startValue;
		public int endValue;
		public volatile int currentValue;
		public AdjustmentType adjustmentType;
		
		private AdjustmentDefinitions(int start, int end, AdjustmentType adjustmentType) {
			this.startValue = start;
			this.endValue = end;
			this.currentValue = start;
			this.adjustmentType = adjustmentType;
		}
	}

	public synchronized boolean runAdjustment(){
		boolean ranPermutation = permutation.iterate();
		
		setAdjustmentValues();
		
		return ranPermutation;
	}
	
	private void setAdjustmentValues(){
		for (Iteration iteration : permutation.getListOfIterations()){
			AdjustmentDefinitions adjustment = (AdjustmentDefinitions) iteration.getRequest();
			
			if (iteration.signalTypeMetric != null && adjustment.adjustmentType == AdjustmentType.long_entry){
				iteration.signalTypeMetric.pointToSignalLongEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment.adjustmentType == AdjustmentType.long_exit){
				iteration.signalTypeMetric.pointToSignalLongExit = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment.adjustmentType == AdjustmentType.short_entry){
				iteration.signalTypeMetric.pointToSignalShortEntry = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment.adjustmentType == AdjustmentType.short_exit){
				iteration.signalTypeMetric.pointToSignalShortExit = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment.adjustmentType == AdjustmentType.signal_control){
				SignalControl.maxStopLossValue = (int) permutation.getIteration(adjustment).getCurrentValue();
			}
		
			else {
				throw new UnsupportedOperationException("Undefined condition: " + adjustment.name());
			}
			
			Co.println("******** Changed " + iteration.signalTypeMetric.name() + " - " + adjustment.name() + " to " +  (int) permutation.getIteration(adjustment).getCurrentValue());

		}
		
		Co.println("\n");
	}
	
	public synchronized int getAdjustmentValueOfInt(AdjustmentDefinitions adjustment){
		return adjustment.currentValue;
	}
	
	public synchronized double getPercentComplete(){
		return permutation.getPercentComplete();
	}
}
