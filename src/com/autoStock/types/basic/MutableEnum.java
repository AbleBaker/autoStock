package com.autoStock.types.basic;

/**
 * @author Kevin Kowalewski
 *
 */
public class MutableEnum<T extends Enum<T>> {
	public T enumValue;
	
	public MutableEnum(T enumObject){
		this.enumValue = enumObject;
	}
}
