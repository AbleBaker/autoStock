package com.autoStock.adjust;

import java.util.concurrent.atomic.AtomicInteger;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentDefinitions;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;

/**
 * @author Kevin Kowalewski
 *
 */
public class Iteration{
	public int start;
	public int end;
	private AtomicInteger current = new AtomicInteger();
	public AdjustmentDefinitions adjustment;
	public SignalMetricType signalTypeMetric;
	
	public Iteration(int start, int end, AdjustmentDefinitions adjustment){
		this.start = start;
		this.end = end;
		this.adjustment = adjustment;
	}
	
	public Iteration(AdjustmentDefinitions adjustment, SignalMetricType signalTypeMetric){
		this.start = adjustment.startValue;
		this.end = adjustment.endValue;
		this.adjustment = adjustment;
		this.signalTypeMetric = signalTypeMetric;
	}
	
	public synchronized int getCurrentValue(){
		return this.current.get();
	}
	
	public synchronized void setCurrentValue(int value){
		this.current.set(value);
	}
	
	public Object getRequest(){
		return this.adjustment;
	}
}