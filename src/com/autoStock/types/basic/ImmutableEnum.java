package com.autoStock.types.basic;

/**
 * @author Kevin Kowalewski
 *
 */
public class ImmutableEnum<E extends Enum<E>> {
	public E enumValue;
	
	public ImmutableEnum(E enumObject){
		this.enumValue = enumObject;
	}
	
	public ImmutableEnum copy(){
		return new ImmutableEnum<E>(enumValue);
	}
}
