package com.autoStock.exchange.request;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestHolder {
	public int requestId;
	public Object callback;
	public Object caller;
	
	public RequestHolder(Object callback){
		this.requestId = RequestManager.getNewRequestId();
		this.callback = callback;
		RequestManager.addRequestHolder(this);
	}
}