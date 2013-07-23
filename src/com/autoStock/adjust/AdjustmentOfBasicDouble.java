package com.autoStock.adjust;

import com.autoStock.types.basic.ImmutableDouble;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentOfBasicDouble extends AdjustmentBase {
	private ImmutableDouble immutableDouble;
	
	public AdjustmentOfBasicDouble(String description, ImmutableDouble immutableDouble, IterableOfDouble iterableOfDouble){
		this.description = description;
		this.iterableBase = iterableOfDouble;
		this.immutableDouble = immutableDouble;
	}
	
	@Override
	public void applyValue() {
		immutableDouble.value = ((IterableOfDouble)iterableBase).getDouble();
	}
	

	public double getValue() {
		return ((IterableOfDouble)iterableBase).getDouble();
	}
}
