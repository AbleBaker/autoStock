package com.autoStock.adjust;

import java.util.ArrayList;

import com.autoStock.Co;
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
////		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_di));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_di));
//		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_cci));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_cci));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_trix));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_trix));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_macd));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_macd));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_rsi));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_rsi));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_control_stop_loss, SignalMetricType.metric_none));
//		
		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_rsi));
		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_rsi));
//		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_mfi));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_mfi));
//		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_roc));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_roc));
		
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_entry, SignalMetricType.metric_willr));
//		permutation.addIteration(new Iteration(AdjustmentDefinitions.algo_signal_metric_long_exit, SignalMetricType.metric_willr));
	}
	
	public void prepare(){
		permutation.prepare();
	}
	
	public ArrayList<Iteration> getListOfIterations(){
		return permutation.getListOfIterations();
	}
	
	public ArrayList<Iteration> getListOfClonedIterations(){
		ArrayList<Iteration> listOfIteration = new ArrayList<Iteration>();
		
		for (Iteration iteration : permutation.getListOfIterations()){
			listOfIteration.add(iteration.clone());
		}
		
		return listOfIteration;
	}
	
	public enum AdjustmentType{
		long_entry,
		long_exit,
		short_entry,
		short_exit,
		signal_control,
	}
	
	public enum AdjustmentDefinitions {		
		algo_signal_metric_long_entry(-25, 50, AdjustmentType.long_entry),
		algo_signal_metric_long_exit(-25, 50, AdjustmentType.long_exit),
//		algo_signal_metric_short_entry(-50, 50, AdjustmentType.short_entry),
//		algo_signal_metric_short_exit(-50, 50, AdjustmentType.short_exit),
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
		public AdjustmentType adjustmentType;
		
		private AdjustmentDefinitions(int start, int end, AdjustmentType adjustmentType) {
			this.startValue = start;
			this.endValue = end;
			this.adjustmentType = adjustmentType;
		}
	}

	public synchronized boolean runAdjustment(){
		boolean ranPermutation = permutation.iterate();
		
		if (ranPermutation){
			setAdjustmentValues();
		}
		
		return ranPermutation;
	}
	
	private void setAdjustmentValues(){
		setAdjustmentValuesFromIterationList(permutation.getListOfIterations());
	}
	
	public void setAdjustmentValuesFromIterationList(ArrayList<Iteration> listOfIteration){
		for (Iteration iteration : listOfIteration){
			AdjustmentDefinitions adjustment = (AdjustmentDefinitions) iteration.getRequest();
			
			if (iteration.signalTypeMetric != null && adjustment.adjustmentType == AdjustmentType.long_entry){
				iteration.signalTypeMetric.pointToSignalLongEntry = iteration.getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment.adjustmentType == AdjustmentType.long_exit){
				iteration.signalTypeMetric.pointToSignalLongExit = iteration.getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment.adjustmentType == AdjustmentType.short_entry){
				iteration.signalTypeMetric.pointToSignalShortEntry = iteration.getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment.adjustmentType == AdjustmentType.short_exit){
				iteration.signalTypeMetric.pointToSignalShortExit = iteration.getCurrentValue();
			}
			
			else if (iteration.signalTypeMetric != null && adjustment.adjustmentType == AdjustmentType.signal_control){
				SignalControl.maxStopLossValue =  iteration.getCurrentValue();
			}
		
			else {
				throw new UnsupportedOperationException("Undefined condition: " + adjustment.name());
			}
			
			Co.println("******** Changed " + iteration.signalTypeMetric.name() + " - " + adjustment.name() + " to " + iteration.getCurrentValue());

		}
		
		Co.println("\n");
	}
	
	public synchronized double getPercentComplete(){
		return permutation.getPercentComplete();
	}
	
	public int getPermutationCount(){
		return permutation.getPermutationCount();
	}
}
