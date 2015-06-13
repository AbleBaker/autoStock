package com.autoStock.types;

import com.autoStock.finance.SecurityTypeHelper.SecurityType;

/**
 * @author Kevin Kowalewski
 *
 */
public class Symbol {
	public String symbolName;
	public SecurityType securityType;

	public Symbol(String symbol, SecurityType securityType) {
		this.symbolName = symbol;
		this.securityType = securityType;
	}
	
	public Symbol(String symbol) {
		this.symbolName = symbol;
		securityType = SecurityType.type_stock;
	}
	
	@Override
	public String toString() {
		return symbolName + ", " + securityType.name();
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((Symbol)obj).symbolName.equals(symbolName);
	}
	
	@Override
	public int hashCode() {
		return symbolName.hashCode();
	}
}
