package com.autoStock.adjust;

import com.autoStock.adjust.Permutation.Iteration;
import com.autoStock.signal.SignalControl;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentCampaign {
	private static AdjustmentCampaign instance = new AdjustmentCampaign();
	private int adjustmentRun = 0;
	private Permutation iterationMatrix = new Permutation();
	
	public AdjustmentCampaign(){
//		iterationMatrix.addIteration(new Iteration(1, 3, 1, AdjustmentDefinitions.algo_signal_sell));
//		iterationMatrix.addIteration(new Iteration(1, 3, 1, AdjustmentDefinitions.algo_signal_buy));
	}
	
	public static AdjustmentCampaign getInstance(){
		return instance;
	}
	
	public enum AdjustmentDefinitions {
		algo_signal_buy(0, 100),
		algo_signal_short(-100, 0),
		algo_signal_sell(-100, 0),
		signal_cci_average(0, 10),
		signal_di_average(0, SignalControl.periodWindow),
		analysis_macd_fast(1, 15),
		analysis_macd_slow(1, 30),
		analysis_macd_signal(1, 30),
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

	public boolean runAdjustment(AdjustmentDefinitions adjustment){
		return iterationMatrix.iterate();
	}
	
	public int getAdjustmentValueOfInt(AdjustmentDefinitions adjustment){
		return adjustment.currentValue;
	}
	
//	public static class Adjustment {
//		public Adjustment(AdjustmentDefinitions adjustmentDefinition, int start, int end){
//			
//		}
//	}
}
