/**
 * 
 */
package com.autoStock.algorithm;

import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmBase {
	public boolean canTrade;
	public AlgorithmListener algorithmListener;
	
	public AlgorithmBase(boolean canTrade){
		this.canTrade = canTrade;
	}
	
	public void setAlgorithmListener(AlgorithmListener algorithmListener){
		this.algorithmListener = algorithmListener;
	}
	
	public ReceiverOfQuoteSlice getReceiver(){
		return (ReceiverOfQuoteSlice) this;
	}
}
