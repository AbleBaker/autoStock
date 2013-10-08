package com.autoStock.adjust;

import com.autoStock.Co;


/**
 * @author Kevin Kowalewski
 *
 */
public class IterableOfInteger extends IterableBase {
	private int min;
	private int max;
	private int step;
	private ConditionBase conditionBase;
	
	public IterableOfInteger(int min, int max, int step, boolean iterateCausesRebase){
		this.min = min;
		this.max = max;
		this.step = step;
		this.iterateCausesRebase = iterateCausesRebase;
		
		if ((min - max) % step != 0){
			throw new IllegalArgumentException("Min - max % step: " + min + ", " + max + ", " + ((min - max) % step));
		}
		
		if (iterateCausesRebase){
			rebaseRequired = true;
		}
	}
	
	public void rebase(int min, int max){
//		throw new IllegalStateException();
		if (currentIndex != 0){
//			throw new IllegalStateException("Can't rebase if index isn't 0. Index is: " + currentIndex);
		}
		
		while ((min - max) % step != 0){
//			Co.println("Warning! Max adjusted due to step remainder: Min - max % step: " + min + ", " + max + ", " + step + ", " + Math.abs(((min - max) % step)));
			max++;
		}
		
		this.min = min;
		this.max = max;
	}
	
	public int getMin(){
		return min;
	}
	
	public int getMax(){
		return max;
	}
	
	@Override
	public void iterate() {
		super.iterate();
		
		if (iterateCausesRebase){
			rebaseRequired = true;
		}
	}
	
	public int getInt(){
		return (int) (min + (step * currentIndex));
	}

	@Override
	public boolean hasMore() {
		return currentIndex <= getMaxIndex();
	}

	@Override
	public int getMaxIndex() {
		return (int) (max - min) / step;
	}

	@Override
	public int getMaxValues() {
		return getMaxIndex() +1;
	}
	
	public IterableOfInteger withCondition(ConditionBase conditionBase){
		this.conditionBase = conditionBase;
		return this;
	}

	@Override
	public boolean isDone() {
		return currentIndex == getMaxIndex();
	}

	@Override
	public boolean skip() {
		return conditionBase != null && !conditionBase.allowedCondition();
	}
}
