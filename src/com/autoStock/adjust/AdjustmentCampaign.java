package com.autoStock.adjust;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentCampaign {
	private static AdjustmentCampaign instance = new AdjustmentCampaign();
	private int adjustmentRun = 0;
	
	public static AdjustmentCampaign getInstance(){
		return instance;
	}
	
	public static enum AdjustmentDefinitions {
		algo_signal_buy(0,100),
		algo_signal_short(-100,0),
		algo_signal_sell(-100, 0),
		signal_cci_average(0, 10),
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
		adjustment.currentValue = adjustment.startValue + adjustmentRun;
		if(adjustment.currentValue == adjustment.endValue){
			return false;
		}
		
		adjustmentRun++;
		
		return true;
//		for (AdjustmentDefinitions adjustment : AdjustmentDefinitions.values()){
//			adjustment.currentValue = adjustment.startValue + adjustmentRun;
//		}
	}
	
	public int getAdjustmentValueOfInt(AdjustmentDefinitions adjustment){
		return adjustment.currentValue;
	}
}
