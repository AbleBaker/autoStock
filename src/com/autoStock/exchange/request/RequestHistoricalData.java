package com.autoStock.exchange.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.Log;
import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.listener.RequestHistoricalDataListener;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.tools.ListTools;
import com.autoStock.tools.ReflectiveComparator;
import com.autoStock.trading.platform.ib.definitions.HistoricalData;
import com.autoStock.trading.results.ExResultHistoricalData;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultRowHistoricalData;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultSetHistoricalData;
import com.autoStock.trading.types.TypeHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestHistoricalData {
	public RequestHolder requestHolder;
	public RequestHistoricalDataListener requestHistoricalDataListener;
	public TypeHistoricalData typeHistoricalData;
	public ExResultSetHistoricalData exResultSetHistoricalData;
	
	public RequestHistoricalData(RequestHolder requestHolder, RequestHistoricalDataListener requestListener, TypeHistoricalData typeHistoricalData){
		this.requestHolder = requestHolder;
		this.requestHistoricalDataListener = requestListener;
		this.typeHistoricalData = typeHistoricalData;
		this.exResultSetHistoricalData = new ExResultHistoricalData(). new ExResultSetHistoricalData(typeHistoricalData);
		this.requestHolder.caller = this;
		
		//Co.println("Start / end date: " + this.typeHistoricalData.startDate + "," + this.typeHistoricalData.endDate);
		//Co.println("Sample period: " + this.typeHistoricalData.duration);
		//Co.println("Best res: " + HistoricalData.getBestResolution(this.typeHistoricalData.duration));
		
		if (HistoricalData.getBestResolution(this.typeHistoricalData.duration) != this.typeHistoricalData.resolution){		
			int neededCalls = (int)(this.typeHistoricalData.duration / HistoricalData.getBestPeriod(typeHistoricalData.resolution).duration) + 1;
			
			requestHolder.mulitpleRequests = neededCalls;
			
			long neededDuration = this.typeHistoricalData.duration;
			long callStartTime = this.typeHistoricalData.startDate.getTime() / 1000;
			long callEndTime = (this.typeHistoricalData.startDate.getTime() / 1000) + HistoricalData.getBestPeriod(typeHistoricalData.resolution).duration;
			
			for (int i=0; i<neededCalls; i++){
				if (i == neededCalls-1){
					callEndTime = callStartTime + neededDuration;
				}
								
				TypeHistoricalData tempTypeHistoricalData = typeHistoricalData.clone();
				tempTypeHistoricalData.duration = callEndTime - callStartTime;
				
				if (tempTypeHistoricalData.duration < HistoricalData.MIN_PERIOD){
					Log.w("Period is too short, using MIN_PERIOD instead");
					callEndTime += (HistoricalData.MIN_PERIOD - tempTypeHistoricalData.duration);
					tempTypeHistoricalData.duration = HistoricalData.MIN_PERIOD;
				}
								
				tempTypeHistoricalData.startDate = new Date(callStartTime*1000);
				tempTypeHistoricalData.endDate = new Date(callEndTime*1000);
				
				ExchangeController.getIbExchangeInstance().getHistoricalPrice(tempTypeHistoricalData, requestHolder);
				
				neededDuration -= HistoricalData.getBestPeriod(typeHistoricalData.resolution).duration;
				callStartTime += HistoricalData.getBestPeriod(typeHistoricalData.resolution).duration + 1;
				callEndTime += HistoricalData.getBestPeriod(typeHistoricalData.resolution).duration + 1;			
			}
		}else{
			ExchangeController.getIbExchangeInstance().getHistoricalPrice(typeHistoricalData, requestHolder);
		}
	}
	
	public synchronized void addResult(ExResultRowHistoricalData exResultRowHistoricalData){
		this.exResultSetHistoricalData.listOfExResultRowHistoricalData.add(exResultRowHistoricalData);
	}
	
	public synchronized void finished(){
		this.requestHolder.mulitpleRequests--;
		if (this.requestHolder.mulitpleRequests <= 0){
			Collections.sort(exResultSetHistoricalData.listOfExResultRowHistoricalData, new ReflectiveComparator(). new ListComparator("date"));
			this.requestHistoricalDataListener.completed(requestHolder, exResultSetHistoricalData);
		}
	}
}
