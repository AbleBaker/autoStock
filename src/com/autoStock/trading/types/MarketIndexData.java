/**
 * 
 */
package com.autoStock.trading.types;

import com.autoStock.types.Exchange;
import com.autoStock.types.Index;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class MarketIndexData implements Cloneable {
	public String securityType;
	public Exchange exchange;
	public Index index;
	
	public MarketIndexData(Exchange exchange, Index index, String securityType){
		this.exchange = exchange;
		this.index = index;
		this.securityType = securityType;
	}
	
	public MarketIndexData clone(){
		try {
			return (MarketIndexData) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
