package com.autoStock.exchange.request.base;

import com.autoStock.exchange.request.RequestManager;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestHolder {
	public int requestId;
	public Object callback;
	public Object caller;
	public int mulitpleRequests = 0;
	
	public RequestHolder(Object callback){
		this.requestId = RequestManager.getNewRequestId();
		this.callback = callback;
		RequestManager.addRequestHolder(this);
	}
}
