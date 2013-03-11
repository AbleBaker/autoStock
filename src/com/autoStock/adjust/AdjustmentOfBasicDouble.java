package com.autoStock.adjust;

import com.autoStock.types.basic.ImmutableDouble;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentOfBasicDouble extends AdjustmentBase {
	private ImmutableDouble immutableDouble;
	
	public AdjustmentOfBasicDouble(ImmutableDouble immutableDouble, IterableOfDouble iterableOfDouble){
		this.iterableBase = iterableOfDouble;
		this.immutableDouble = immutableDouble;
	}
	
	@Override
	public void applyValue() {
		immutableDouble.value = ((IterableOfDouble)iterableBase).getDouble();
	}
}
