/**
 * 
 */
package com.autoStock.algorithm;

import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmBase {
	public boolean canTrade;
	public AlgorithmListener algorithmListener;
	public Exchange exchange;
	public Symbol symbol;
	public boolean isActive = true;
	
	public AlgorithmBase(boolean canTrade, Exchange exchange, Symbol symbol){
		this.canTrade = canTrade;
		this.exchange = exchange;
		this.symbol = symbol;
	}
	
	public void setAlgorithmListener(AlgorithmListener algorithmListener){
		this.algorithmListener = algorithmListener;
	}
	
	public ReceiverOfQuoteSlice getReceiver(){
		return (ReceiverOfQuoteSlice) this;
	}
}
