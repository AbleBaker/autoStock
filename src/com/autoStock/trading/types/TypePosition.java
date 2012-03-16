/**
 * 
 */
package com.autoStock.trading.types;

import com.lowagie.text.Section;

/**
 * @author Kevin Kowalewski
 *
 */
public class TypePosition {
	public int units;
	public String symbol;
	public String securityType;
	public double pricePosition;
	public double lastKnownPrice;
	
	public TypePosition(){
		
	}

	public TypePosition(int units, String symbol, String securityType, double averagePrice) {
		this.units = units;
		this.symbol = symbol;
		this.securityType = securityType;
		this.pricePosition = averagePrice;
	}
}
