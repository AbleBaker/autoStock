package com.autoStock.adjust;

import com.autoStock.Co;
import com.autoStock.adjust.AdjustmentInterfaces.AdjustmentInterfaceForInteger;
import com.autoStock.types.basic.ImmutableDouble;
import com.autoStock.types.basic.ImmutableInteger;

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
