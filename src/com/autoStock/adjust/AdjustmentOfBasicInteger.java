package com.autoStock.adjust;

import com.autoStock.adjust.AdjustmentInterfaces.AdjustmentInterfaceForInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentOfBasicInteger extends AdjustmentBase {
	private AdjustmentInterfaceForInteger adjustmentInterfaceForInteger;
	
	public AdjustmentOfBasicInteger(Integer integer, AdjustmentInterfaceForInteger adjustmentInterfaceForInteger, IterableBase iterableBase){
		this.adjustmentInterfaceForInteger = adjustmentInterfaceForInteger;
		this.iterableBase = iterableBase;
	}
	
	@Override
	public void applyValue() {
		
	}
}
