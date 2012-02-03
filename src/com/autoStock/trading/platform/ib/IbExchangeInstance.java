/**
 * 
 */
package com.autoStock.trading.platform.ib;

import com.autoStock.Co;
import com.autoStock.exchange.request.RequestHolder;
import com.autoStock.trading.platform.ib.core.Contract;
import com.autoStock.trading.platform.ib.core.EClientSocket;
import com.autoStock.trading.types.TypeHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class IbExchangeInstance {
	public IbExchangeWrapper ibExchangeWrapper;
	public IbExchangeClientSocket ibExchangeClientSocket;
	
	public void init(){
		ibExchangeWrapper = new IbExchangeWrapper();
		ibExchangeClientSocket = new IbExchangeClientSocket();
		
		try {
			ibExchangeClientSocket.init(ibExchangeWrapper);
			ibExchangeClientSocket.connect();
		}catch(Exception e){e.printStackTrace();}
	}
	
	public EClientSocket getEclientSocket(){
		return ibExchangeClientSocket.eClientSocket;
	}
	
	public void getHistoricalPrice(TypeHistoricalData typeHistoricalData, RequestHolder requestHolder){
		Co.println("Request id: " + requestHolder.requestId);
		Contract contract = new Contract();
		contract.m_exchange = "Smart";
		contract.m_symbol = "AAPL";
		contract.m_secType = "STK";
		contract.m_currency = "USD";
		String endDate = "20120109 10:30:00 EST";
		String duration = "120 S";
		ibExchangeClientSocket.eClientSocket.reqHistoricalData(requestHolder.requestId, contract, endDate, duration, "1 secs", "BID_ASK", 1, 2);
	}
}
