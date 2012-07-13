/**
 * 
 */
package com.autoStock.trading.platform.ib;

import java.text.SimpleDateFormat;

import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.internal.Config;
import com.autoStock.trading.platform.ib.core.Contract;
import com.autoStock.trading.platform.ib.core.EClientSocket;
import com.autoStock.trading.platform.ib.core.Order;
import com.autoStock.trading.platform.ib.core.ScannerSubscription;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.MarketData;
import com.autoStock.trading.types.Position;
import com.autoStock.trading.types.RealtimeData;

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
		}catch(Exception e){} //e.printStackTrace();
	}
	
	public EClientSocket getEclientSocket(){
		return ibExchangeClientSocket.eClientSocket;
	}
	
	public void getAccountUpdates(){
		ibExchangeClientSocket.eClientSocket.reqAccountUpdates(true, Config.plIbUsername);
	}

	public void getOpenOrders(){
		ibExchangeClientSocket.eClientSocket.reqOpenOrders();
	}
	
	public void placeBuyOrder(Position typePosition, RequestHolder requestHolder){
		Contract contract = new Contract();
		Order order = new Order();
		contract.m_exchange = "NYSE";
		contract.m_symbol = typePosition.symbol;
		contract.m_secType = "STK";
		contract.m_currency = "USD";
		order.m_action = "BUY";
		order.m_orderType = "MKT";
		order.m_auxPrice = 0.01;
		order.m_totalQuantity = typePosition.units;
		
		ibExchangeClientSocket.eClientSocket.placeOrder(requestHolder.requestId, contract, order);
	}
	
	public void placeSellOrder(Position typePosition, RequestHolder requestHolder){
		Contract contract = new Contract();
		Order order = new Order();
		contract.m_exchange = "NYSE";
		contract.m_symbol = typePosition.symbol;
		contract.m_secType = "STK";
		contract.m_currency = "USD";
		order.m_action = "SELL";
		order.m_orderType = "MKT";
		order.m_auxPrice = 0.1;
		order.m_totalQuantity = typePosition.units;
		
		ibExchangeClientSocket.eClientSocket.placeOrder(requestHolder.requestId, contract, order);
	}
	
	public void placeShortOrder(Position typePosition, RequestHolder requestHolder){
		Contract contract = new Contract();
		Order order = new Order();
		contract.m_exchange = "NYSE";
		contract.m_symbol = typePosition.symbol;
		contract.m_secType = "STK";
		contract.m_currency = "USD";
		order.m_action = "SELL";
		order.m_orderType = "MKT";
		order.m_auxPrice = 0.1;
		order.m_totalQuantity = typePosition.units;
		
		ibExchangeClientSocket.eClientSocket.placeOrder(requestHolder.requestId, contract, order);
	}
	
	public void getScanner(){
		ScannerSubscription scanner = new ScannerSubscription();
		scanner.numberOfRows(50);
		scanner.instrument("STOCK.HK");
		scanner.locationCode("STK.HK.ASX");
		scanner.scanCode("TOP_OPEN_PERC_GAIN");
		scanner.aboveVolume(10000);
		scanner.abovePrice(3.00);
		scanner.averageOptionVolumeAbove(0);
		ibExchangeClientSocket.eClientSocket.reqScannerSubscription(1, scanner);
	}
	
	public void getRealtimeData(RealtimeData typeRealtimeData, RequestHolder requestHolder){
		//Co.println("Request id: " + requestHolder.requestId);
		Contract contract = new Contract();
		contract.m_exchange = "ASX";
		contract.m_symbol = typeRealtimeData.symbol;
		contract.m_secType = typeRealtimeData.securityType;
		contract.m_currency = "AUD";
		ibExchangeClientSocket.eClientSocket.reqRealTimeBars(requestHolder.requestId, contract, 5, "TRADES", false);
	}
	
	public void getMarketData(MarketData typeMarketData, RequestHolder requestHolder){
		//Co.println("Request id: " + requestHolder.requestId);
		Contract contract = new Contract();
		contract.m_exchange = "ASX";
		contract.m_symbol = typeMarketData.symbol;
		contract.m_secType = typeMarketData.securityType;
		contract.m_currency = "AUD";
		ibExchangeClientSocket.eClientSocket.reqMktData(requestHolder.requestId, contract, "104,165,225", false);
	}
	
	public void getHistoricalPrice(HistoricalData typeHistoricalData, RequestHolder requestHolder){
		//Co.println("Request id: " + requestHolder.requestId);
		Contract contract = new Contract();
		contract.m_exchange = "Smart";
		contract.m_symbol = typeHistoricalData.symbol;
		contract.m_secType = typeHistoricalData.securityType;
		contract.m_currency = "USD";
		String endDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(typeHistoricalData.endDate) + " est";
		String duration = String.valueOf(typeHistoricalData.duration) + " S";
		ibExchangeClientSocket.eClientSocket.reqHistoricalData(requestHolder.requestId, contract, endDate, duration, typeHistoricalData.resolution.barSize, "TRADES", 1, 2);
	}
}
