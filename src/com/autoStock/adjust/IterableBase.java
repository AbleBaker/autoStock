package com.autoStock.adjust;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 * 
 */
public abstract class IterableBase {
	protected int currentIndex = 0;
	
	public abstract boolean hasMore();
	public abstract boolean isDone();
	public abstract boolean skip();
	public abstract int getMaxIndex();
	public abstract int getMaxValues();

	public void iterate() {
		currentIndex++;
	}

	public void reset() {
		currentIndex = 0;
	}
	
	public void overrideAndSetCurrentIndex(int currentIndex){
		this.currentIndex = currentIndex;
	}
}
