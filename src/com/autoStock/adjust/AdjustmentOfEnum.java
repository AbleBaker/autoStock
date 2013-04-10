package com.autoStock.adjust;

import com.autoStock.types.basic.ImmutableEnum;

/**
 * @author Kevin Kowalewski
 *
 */
public class AdjustmentOfEnum<E extends Enum<E>> extends AdjustmentBase {
	private ImmutableEnum<E> immutableEnum;
	
	public AdjustmentOfEnum(String description, IterableOfEnum<E> iterableOfEnum, ImmutableEnum<E> immutableEnum){
		this.iterableBase = iterableOfEnum;
		this.description = description;
		this.immutableEnum = immutableEnum;
	}
	
	@Override
	public void applyValue() {
		 immutableEnum.enumValue = getValue();
	}
	
	public E getValue(){
		return ((IterableOfEnum<E>)iterableBase).getEnum();
	}
}
