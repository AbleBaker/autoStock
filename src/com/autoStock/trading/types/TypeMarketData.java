/**
 * 
 */
package com.autoStock.trading.types;

/**
 * @author Kevin Kowalewski
 *
 */
public class TypeMarketData implements Cloneable {
	public String symbol;
	public String securityType;
	
	public TypeMarketData(String symbol, String securityType){
		this.symbol = symbol;
		this.securityType = securityType;
	}
	
	public TypeMarketData clone(){
		try {
			return (TypeMarketData) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
