package com.autoStock.types.basic;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 *
 */
public class ImmutableDouble {
	public double value;
	
	public ImmutableDouble(){
		value = 0;
	}
	
	public ImmutableDouble(double value){
		this.value = value;
	}
}
