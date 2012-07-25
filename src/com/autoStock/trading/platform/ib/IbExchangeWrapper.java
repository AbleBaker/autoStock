/**
 * 
 */
package com.autoStock.trading.platform.ib;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.exchange.request.RequestHistoricalData;
import com.autoStock.exchange.request.RequestManager;
import com.autoStock.exchange.request.RequestMarketData;
import com.autoStock.exchange.request.RequestMarketOrder;
import com.autoStock.exchange.request.RequestMarketScanner;
import com.autoStock.exchange.results.ExResultHistoricalData;
import com.autoStock.exchange.results.ExResultMarketData.ExResultRowMarketData;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultRowMarketOrder;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultRowMarketScanner;
import com.autoStock.trading.platform.ib.core.Contract;
import com.autoStock.trading.platform.ib.core.ContractDetails;
import com.autoStock.trading.platform.ib.core.EWrapper;
import com.autoStock.trading.platform.ib.core.Execution;
import com.autoStock.trading.platform.ib.core.Order;
import com.autoStock.trading.platform.ib.core.OrderState;
import com.autoStock.trading.platform.ib.core.UnderComp;
import com.autoStock.trading.platform.ib.definitions.MarketDataDefinitions;

/**
 * @author Kevin Kowalewski
 *
 */
public class IbExchangeWrapper implements EWrapper {
	
	public static ArrayList<Integer> discardErrorCodes = new ArrayList<Integer>();
	
	public IbExchangeWrapper(){
		discardErrorCodes.add(2104);
		discardErrorCodes.add(2107);
		discardErrorCodes.add(2106);
		discardErrorCodes.add(2108);
	}

	@Override
	public void error(Exception e) {
		Co.log("Exception occurred");
		e.printStackTrace();
	}

	@Override
	public void error(String str) {
		Co.log("Error occurred: " + str);		
	}

	@Override
	public void error(int id, int errorCode, String errorMsg) {
		if (!discardErrorCodes.contains(errorCode)){
			Co.log("Error occurred:" + errorCode + "," + errorMsg);
		}
	}

	@Override
	public void connectionClosed() {
		Co.log("Connection Closed");
	}

	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		Co.log("Got tickPrice: " + tickerId + ", " + field + ", " + price + ", " + MarketDataDefinitions.getTickPriceField(field).name());
		((RequestMarketData)RequestManager.getRequestHolder(tickerId).caller).addResult(new ExResultRowMarketData(MarketDataDefinitions.getTickPriceField(field), price));
	}

	@Override
	public void tickSize(int tickerId, int field, int size) {
		Co.log("Got tickSize: " + tickerId + ", " + field + ", " + size + ", " + MarketDataDefinitions.getTickSizeField(field));
		((RequestMarketData)RequestManager.getRequestHolder(tickerId).caller).addResult(new ExResultRowMarketData(MarketDataDefinitions.getTickSizeField(field), size));
	}

	@Override
	public void tickGeneric(int tickerId, int tickType, double value) {
		Co.log("Got tickGeneric: " + tickerId + ", " + tickType + ", " + value);
	}
	
	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
		Co.log("Got tickOptionCompuation");
	}

	@Override
	public void tickString(int tickerId, int tickType, String value) {
		Co.log("Got tickString: " + tickerId + "," + value);
		((RequestMarketData)RequestManager.getRequestHolder(tickerId).caller).addResult(new ExResultRowMarketData(value));
	}

	@Override
	public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry) {
		Co.log("Got tickEFP");
	}

	@Override
	public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
		Co.log("Got orderStatus: " + orderId +", " + status + ", " + filled + ", " + remaining + ", " + avgFillPrice + ", " + lastFillPrice + ", " + whyHeld);
		
		if (status.equals("Filled")){
			((RequestMarketOrder)RequestManager.getRequestHolder(orderId).caller).finished();
		}else{
			((RequestMarketOrder)RequestManager.getRequestHolder(orderId).caller).addResult(new ExResultRowMarketOrder(avgFillPrice, lastFillPrice, 0, filled, remaining, filled-remaining, status));
		}
	}

	@Override
	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
		Co.log("Got openOrder: " + contract.m_symbol + ", " + orderState.m_commission + ", " + orderState.m_status);
	}

	@Override
	public void openOrderEnd() {
		//Co.log("Got openOrderEnd");
	}

	@Override
	public void updateAccountValue(String key, String value, String currency, String accountName) {
		Co.log("Got updateAccountValue: " + key + ", " + value + ", " + ", " + currency + ", " + accountName);
	}

	@Override
	public void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
		Co.log("Got updatePortfolio");
	}

	@Override
	public void updateAccountTime(String timeStamp) {
		Co.log("Got updateAccountTime");
	}

	@Override
	public void accountDownloadEnd(String accountName) {
		Co.log("Got accountDownloadEnd");
	}

	@Override
	public void nextValidId(int orderId) {
		//Co.log("Got nextValidId: " + orderId);	
	}

	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		Co.log("Got contractDetails");
	}

	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		Co.log("Got bondContractDetails");	
	}

	@Override
	public void contractDetailsEnd(int reqId) {
		Co.log("Got contractDetailsEnd");
	}

	@Override
	public void execDetails(int reqId, Contract contract, Execution execution) {
		Co.log("Got execDetails: " + contract.m_symbol + ", " + execution.m_avgPrice + ", " + execution.m_cumQty + ", " + execution.m_price + ", " + execution.m_shares);
	}

	@Override
	public void execDetailsEnd(int reqId) {
		Co.log("Got execDetailsEnd");
	}

	@Override
	public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
		Co.log("Got updateMktDepth");
	}

	@Override
	public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {
		Co.log("Got updateMktDepthL2");	
	}

	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
		Co.log("Got updateNewsBulletin");
	}

	@Override
	public void managedAccounts(String accountsList) {
		Co.log("Got managedAccounts");
	}

	@Override
	public void receiveFA(int faDataType, String xml) {
		Co.log("Got recieveFA");
	}

	@Override
	public void historicalData(int requestId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {
		//Co.log("Got historicalData:" + requestId + date + "," + open + "," + high + "," + low + "," + close + "," + volume + "," + count + "," + WAP + "," + hasGaps);
		if (date.contains("finished")){
			((RequestHistoricalData)RequestManager.getRequestHolder(requestId).caller).finished();
		}else{
			((RequestHistoricalData)RequestManager.getRequestHolder(requestId).caller).addResult(new ExResultHistoricalData(). new ExResultRowHistoricalData(Long.valueOf(date), WAP, volume, count));
		}
	}

	@Override
	public void scannerParameters(String xml) {
		Co.log("Got scannerParameters: " + xml);		
	}

	@Override
	public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
		Co.log("Got scannerData: " + rank + ", " + contractDetails.m_summary.m_symbol + ", " + distance + ", " + benchmark + ", " + projection + ", " + legsStr);
		if (rank == 0){((RequestMarketScanner)RequestManager.getRequestHolder(reqId).caller).clearResults();}
		((RequestMarketScanner)RequestManager.getRequestHolder(reqId).caller).addResult(new ExResultRowMarketScanner(contractDetails.m_summary.m_symbol, rank));
	}

	@Override
	public void scannerDataEnd(int reqId) {
		Co.log("Got scannerDataEnd");
		((RequestMarketScanner)RequestManager.getRequestHolder(reqId).caller).finished();
	}

	@Override
	public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
		Co.log("Got realtimeBar: " + reqId + "," + time + "," + open + "," + high + "," + low + "," + close + "," + volume + "," + count);
	}

	@Override
	public void currentTime(long time) {
		Co.log("Got currentTime");
	}

	@Override
	public void fundamentalData(int reqId, String data) {
		Co.log("Got fundementalData");
	}

	@Override
	public void deltaNeutralValidation(int reqId, UnderComp underComp) {
		Co.log("Got deltaNeutralValidation");
	}

	@Override
	public void tickSnapshotEnd(int reqId) {
		Co.log("Got tickSnapshotEnd");
	}

	@Override
	public void marketDataType(int reqId, int marketDataType) {
		Co.log("Got marketDataType");
		
	}
}
