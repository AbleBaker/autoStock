package com.autoStock.signal;

public class SignalRangeLimit {
	private int max;
	private int min;
	
	public SignalRangeLimit(){
		reset();
	}
	
	public SignalRangeLimit(int min, int max){
		this.min = min;
		this.max = max;
	}
	
	public void addValue(int value){
		min = Math.min(min, value);
		max = Math.max(max, value);
	}
	
	public boolean isSet(){
		if (min != Integer.MAX_VALUE && max != Integer.MIN_VALUE){
			return true;
		}
		return false;
	}
	
	public void reset(){
		min = Integer.MAX_VALUE;
		max = Integer.MIN_VALUE;
	}	
	
	public int getMin(){
		if (min == Integer.MAX_VALUE){throw new IllegalAccessError("Can't access min because it was never set");}
		return min;
	}
	
	public int getMax(){
		if (max == Integer.MIN_VALUE){throw new IllegalAccessError("Can't access max because it was never set");}
		return max;
	}
	
	public SignalRangeLimit copy(){
		return new SignalRangeLimit(min, max);
	}
}
