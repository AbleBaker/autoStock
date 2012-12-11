package com.autoStock.adjust;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class AdjustmentBase {
	protected IterableBase iterableBase;
	public abstract void applyValue();
	
	public IterableBase getIterableBase(){
		return iterableBase;
	}
}
