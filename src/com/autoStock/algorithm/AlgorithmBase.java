/**
 * 
 */
package com.autoStock.algorithm;

import com.autoStock.algorithm.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseReason;
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
	public AlgorithmMode algorithmMode;
	public int transactions = 0;
	public PositionGovernorResponse positionGovernorResponsePrevious = new PositionGovernorResponse();
	public boolean isDisabled = false;
	
	public AlgorithmBase(boolean canTrade, Exchange exchange, Symbol symbol, AlgorithmMode algorithmMode){
		this.canTrade = canTrade;
		this.exchange = exchange;
		this.symbol = symbol;
		this.algorithmMode = algorithmMode;
	}
	
	public void setAlgorithmListener(AlgorithmListener algorithmListener){
		this.algorithmListener = algorithmListener;
	}
	
	public ReceiverOfQuoteSlice getReceiver(){
		return (ReceiverOfQuoteSlice) this;
	}
	
	public void handlePositionChange(){
		transactions++;
	}
	
	public void disable(){
		isDisabled = true;
	}
	
	public void disable(PositionGovernorResponse positionGovernorResponse){
		isDisabled = true;
	}
}
