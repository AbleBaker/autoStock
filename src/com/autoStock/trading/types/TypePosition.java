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
	public float averagePrice;

	public TypePosition(int units, String symbol, String securityType, float averagePrice) {
		this.units = units;
		this.symbol = symbol;
		this.securityType = securityType;
		this.averagePrice = averagePrice;
	}
}
