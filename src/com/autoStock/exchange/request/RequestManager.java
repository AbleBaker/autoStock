package com.autoStock.exchange.request;

import java.util.ArrayList;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestManager {
	private static int requestId;
	private static ArrayList<RequestHolder> listOfRequestHolder = new ArrayList<RequestHolder>();
	
	public static synchronized int getNewRequestId(){
		requestId++;
		return requestId;
	}
	
	public synchronized RequestHolder getRequestHolder(int requestId){
		for (RequestHolder requestHolder : listOfRequestHolder){
			if (requestHolder.requestId == requestId){
				return requestHolder;
			}
		}
		return null;
	}
	
	public synchronized void removeRequestHolder(int requestId){
		RequestHolder requestHolder = getRequestHolder(requestId);
		listOfRequestHolder.remove(requestHolder);
		requestHolder = null;
	}
}
