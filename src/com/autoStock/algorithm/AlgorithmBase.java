/**
 * 
 */
package com.autoStock.algorithm;

import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmBase {
	public boolean canTrade;
	public AlgorithmListener algorithmListener;
	public Exchange exchange;
	
	public AlgorithmBase(boolean canTrade, Exchange exchange){
		this.canTrade = canTrade;
		this.exchange = exchange;
	}
	
	public void setAlgorithmListener(AlgorithmListener algorithmListener){
		this.algorithmListener = algorithmListener;
	}
	
	public ReceiverOfQuoteSlice getReceiver(){
		return (ReceiverOfQuoteSlice) this;
	}
}
