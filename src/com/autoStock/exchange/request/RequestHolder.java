package com.autoStock.exchange.request;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestHolder {
	public int requestId;
	public Object callback;
	
	public RequestHolder(Object callback){
		this.requestId = RequestManager.getNewRequestId();
		this.callback = callback;
	}
}
