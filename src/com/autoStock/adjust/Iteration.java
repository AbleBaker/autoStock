package com.autoStock.adjust;

import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.adjust.AdjustmentCampaign.AdjustmentDefinitions;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.tools.Lock;

/**
 * @author Kevin Kowalewski
 *
 */
public class Iteration implements Cloneable{
	public int start;
	public int end;
	private volatile int current;
	public AdjustmentDefinitions adjustment;
	public SignalMetricType signalTypeMetric;
	public Lock lock = new Lock();
	
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
		synchronized (lock){
			return current;
		}
	}
	
	public synchronized void setCurrentValue(int value){
		synchronized (lock){
			this.current = value;
		}
	}
	
	public Object getRequest(){
		return this.adjustment;
	}
	
	@Override
	public Iteration clone(){
		try {
			return (Iteration) super.clone();
		}catch(CloneNotSupportedException e){}
		return null;
	}
}