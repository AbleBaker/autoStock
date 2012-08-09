/**
 * 
 */
package com.autoStock.trading.types;

import com.autoStock.position.PositionDefinitions.PositionType;


/**
 * @author Kevin Kowalewski
 *
 */
public class Position implements Cloneable {
	public int units;
	public String symbol;
	public String securityType;
	public double price;
	public double lastKnownPrice;
	public PositionType positionType = PositionType.position_none;
	
	public Position(){
		
	}

	public Position(PositionType positionType, int units, String symbol, String securityType, double averagePrice) {
		this.positionType = positionType;
		this.units = units;
		this.symbol = symbol;
		this.securityType = securityType;
		this.price = averagePrice;
	}
	
	@Override
	public Position clone(){
		try {
			return (Position) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException();
		}
	}
}
