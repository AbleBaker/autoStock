package com.autoStock.exchange.request;

import java.util.ArrayList;

import com.autoStock.exchange.request.base.RequestHolder;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestManager {
	private static volatile int requestId;
	private static ArrayList<RequestHolder> listOfRequestHolder = new ArrayList<RequestHolder>();
	
	public static synchronized int getNewRequestId(){
		requestId++;
		return requestId;
	}
	
	public static synchronized void addRequestHolder(RequestHolder requestHolder){
		listOfRequestHolder.add(requestHolder);
	}
	
	public static synchronized RequestHolder getRequestHolder(int requestId){
		for (RequestHolder requestHolder : listOfRequestHolder){
			if (requestHolder.requestId == requestId){
				return requestHolder;
			}
		}
		throw new NullPointerException("No request holder has requestId: " + requestId);
	}
	
	public static synchronized void removeRequestHolder(int requestId){
		RequestHolder requestHolder = getRequestHolder(requestId);
		listOfRequestHolder.remove(requestHolder);
		requestHolder = null;
	}
}
