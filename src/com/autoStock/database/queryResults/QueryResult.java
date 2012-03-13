/**
 * 
 */
package com.autoStock.database.queryResults;

/**
 * @author Kevin Kowalewski
 *
 */
public class QueryResult {
	 public static class QrSymbolCountFromExchange{
		 public int count;
		 public long sizeVolume;
		 public String symbol;
		 
		 public QrSymbolCountFromExchange(String symbol, int count, long sizeVolume) {
			 this.count = count;
			 this.symbol = symbol;
			 this.sizeVolume = sizeVolume;
		 }
	 }
}
