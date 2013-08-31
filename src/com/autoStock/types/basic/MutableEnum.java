package com.autoStock.types.basic;

/**
 * @author Kevin Kowalewski
 *
 */
public class MutableEnum<E extends Enum<E>> {
	public E enumValue;
	
	public MutableEnum(E enumObject){
		this.enumValue = enumObject;
	}
}
