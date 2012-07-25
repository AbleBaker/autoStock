package com.autoStock.exchange;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.exchange.ExchangeStatusListener.ExchangeState;
import com.autoStock.types.Exchange;
import com.autoStock.types.basic.Time;

/**
 * @author Kevin Kowalewski
 *
 */
public class ExchangeStatusObserver {
	private Exchange exchange;
	private ArrayList<ExchangeStatusListener> listOfExchangeStatusListeners = new ArrayList<ExchangeStatusListener>();
	private Thread threadForExchangeObserver;
	private ExchangeState currentExchangeState = ExchangeState.status_unknown;
	private static final int observeDelayMs = 1000;
	
	public ExchangeStatusObserver(Exchange exchange){
		this.exchange = exchange;
	}
	
	public void observeExchangeStatus(){
		if (listOfExchangeStatusListeners.size() == 0){
			throw new IllegalStateException();
		}
		
		threadForExchangeObserver = new Thread(new Runnable() {		
			@Override
			public void run() {
				while (true){
					//Open - already or just happened 
					if ((currentExchangeState == ExchangeState.status_unknown || currentExchangeState == ExchangeState.status_closed) && exchange.isOpen()){
						notifyListeners(ExchangeState.status_open);
						currentExchangeState = ExchangeState.status_open;
					}
					
					//Closed - already or just happened
					else if ((currentExchangeState == ExchangeState.status_unknown || currentExchangeState == ExchangeState.status_close_future) && exchange.isClosed()){
						notifyListeners(ExchangeState.status_open);
						currentExchangeState = ExchangeState.status_closed;
					}
					
					else if (currentExchangeState == ExchangeState.status_open){
						Time timeUntilClose = exchange.getTimeUntil(exchange.getLocalTimeFromForeignTime(exchange.timeClose, exchange.timeZone));
						if (timeUntilClose.isFuture()){
							if (timeUntilClose.hours == 0 && timeUntilClose.minutes < 30){
								notifyListeners(ExchangeState.status_close_future.setTime(timeUntilClose));
								currentExchangeState = ExchangeState.status_close_future.setTime(timeUntilClose);
							}
						}
					}
					
					try {Thread.sleep(observeDelayMs);}catch(InterruptedException e){return;}
				}
			}
		});
		
		threadForExchangeObserver.start();
	}
	
	private void notifyListeners(ExchangeState exchangeState){
		for (ExchangeStatusListener listener : listOfExchangeStatusListeners){
			listener.stateChanged(exchangeState);
		}
	}
	
	public void addListener(ExchangeStatusListener listener){
		listOfExchangeStatusListeners.add(listener);
	}
	
	public void removeListener(ExchangeStatusListener listener){
		listOfExchangeStatusListeners.remove(listener);
	}
}
