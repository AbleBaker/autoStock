/**
 * 
 */
package com.autoStock.trading.types;

/**
 * @author Kevin Kowalewski
 *
 */
public class TypeRealtimeData implements Cloneable {

	public String symbol;
	public String securityType;
	
	public TypeRealtimeData(String symbol, String securityType){
		this.symbol = symbol;
		this.securityType = securityType.toUpperCase();
	}

	public TypeRealtimeData clone(){
		try {
			return (TypeRealtimeData) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
