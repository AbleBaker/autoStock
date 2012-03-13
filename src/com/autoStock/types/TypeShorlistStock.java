/**
 * 
 */
package com.autoStock.types;

import com.autoStock.scanner.Shortlist.ShortlistReason;

/**
 * @author Kevin Kowalewski
 *
 */
public class TypeShorlistStock {
	public String symbol;
	public String exchange;
	public ShortlistReason shortlistReason;
	
	public TypeShorlistStock(String symbol, String exchange, ShortlistReason shortlistReason) {
		this.symbol = symbol;
		this.exchange = exchange;
		this.shortlistReason = shortlistReason;
	}
}
