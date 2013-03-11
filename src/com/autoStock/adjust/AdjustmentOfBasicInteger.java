package com.autoStock.adjust;

import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentOfBasicInteger extends AdjustmentBase {
	private ImmutableInteger immutableInteger;
	
	public AdjustmentOfBasicInteger(String description, ImmutableInteger immutableInteger, IterableOfInteger iterableOfInteger){
		this.iterableBase = iterableOfInteger;
		this.immutableInteger = immutableInteger;
		this.description = description;
	}
	
	@Override
	public void applyValue() {
		immutableInteger.value = getValue();
	}

	public int getValue() {
		return ((IterableOfInteger)iterableBase).getInt();
	}
}
