/**
 * 
 */
package com.autoStock.trading.types;

import com.autoStock.finance.Account;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.tools.MathTools;


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
	
	public Position(){}
	
	public Position(PositionType positionType, int units, String symbol, String securityType, double averagePrice) {
		this.positionType = positionType;
		this.units = units;
		this.symbol = symbol;
		this.securityType = securityType;
		this.price = averagePrice;
	}
	
	public double getPositionEntryPrice(boolean includeTransactionFees){
		return MathTools.round(units * price + (includeTransactionFees ? Account.instance.getTransactionCost(units, price) : 0));
	}
	
	public double getPositionCurrentPrice(boolean includeTransactionFees){		
		return MathTools.round((units * lastKnownPrice) + (includeTransactionFees ? Account.instance.getTransactionCost(units, lastKnownPrice) : 0));
	}
	
	//TODO: Fix this
	public double getPositionEntryValue(boolean includeTransactionFees){
		return MathTools.round(units * price - (includeTransactionFees ? Account.instance.getTransactionCost(units, price) : 0));
	}
	
	//TODO: fix this too 
	public double getPositionCurrentValue(boolean includeTransactionFees){
		return MathTools.round((units * lastKnownPrice) - (includeTransactionFees ? Account.instance.getTransactionCost(units, lastKnownPrice) : 0));
	}
	
	public double getPositionProfitLossAfterComission(){
		return MathTools.round(getPositionCurrentValue(true) - getPositionEntryPrice(true));
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
