/**
 * 
 */
package com.autoStock.trading.types;

/**
 * @author Kevin Kowalewski
 *
 */
public class MarketData implements Cloneable {
	public String symbol;
	public String securityType;
	
	public MarketData(String symbol, String securityType){
		this.symbol = symbol;
		this.securityType = securityType;
	}
	
	public MarketData clone(){
		try {
			return (MarketData) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
