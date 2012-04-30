package com.autoStock.internal;

/**
 * @author Kevin Kowalewski
 *
 */
public class CallbackLock {
	private volatile int callbacks;
	
	public void requestCallbackLock(){
		synchronized (this) {
			callbacks++;
		}
	}
	
	public void releaseCallbackLock(){
		synchronized (this) {
			callbacks--;	
		}
	}
	
	public boolean isWaitingForCallbacks(){
		if (callbacks > 0){
			return true;
		}
		return false;
	}
}
