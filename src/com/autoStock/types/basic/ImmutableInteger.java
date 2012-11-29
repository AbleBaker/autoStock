package com.autoStock.types.basic;

/**
 * @author Kevin Kowalewski
 *
 */
public class ImmutableInteger {
	public int value;
	
	public ImmutableInteger(){
		value = 0;
	}
	
	public ImmutableInteger(int value){
		this.value = value;
	}
}
