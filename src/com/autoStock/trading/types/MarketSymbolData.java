/**
 * 
 */
package com.autoStock.trading.types;

import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class MarketSymbolData implements Cloneable {
	public String securityType;
	public Exchange exchange;
	public Symbol symbol;
	
	public MarketSymbolData(Exchange exchange, Symbol symbol, String securityType){
		this.exchange = exchange;
		this.symbol = symbol;
		this.securityType = securityType;
	}
	
	public MarketSymbolData clone(){
		try {
			return (MarketSymbolData) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
