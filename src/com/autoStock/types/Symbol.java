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
}
