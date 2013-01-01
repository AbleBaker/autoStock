/**
 * 
 */
package com.autoStock.algorithm;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.algorithm.core.AlgorithmChart;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.core.AlgorithmListener;
import com.autoStock.algorithm.core.AlgorithmState;
import com.autoStock.algorithm.core.AlgorithmTable;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.indicator.CommonAnlaysisData;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.position.PositionManager;
import com.autoStock.position.ListenerOfPositionStatusChange;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalGroup;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.strategy.StrategyBase;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.strategy.StrategyResponse.StrategyAction;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmBase implements ListenerOfPositionStatusChange {
	public int periodLength = SignalControl.periodLengthStart.value;
	public Exchange exchange;
	public Symbol symbol;
	public AlgorithmState algorithmState = new AlgorithmState();
	public AlgorithmMode algorithmMode;
	public AlgorithmListener algorithmListener;
	public AlgorithmChart algorithmChart;
	public AlgorithmTable algorithmTable;
	public IndicatorGroup indicatorGroup;
	public SignalGroup signalGroup;
	public PositionGovernorResponse PGResponsePrevious = new PositionGovernorResponse();
	public final CommonAnlaysisData commonAnlaysisData = new CommonAnlaysisData();
	public final ArrayList<QuoteSlice> listOfQuoteSlice = new ArrayList<QuoteSlice>();
	public final ArrayList<StrategyResponse> listOfStrategyResponse = new ArrayList<StrategyResponse>();
	protected ArrayList<SignalMetricType> listOfSignalMetricType = new ArrayList<SignalMetricType>();
	public QuoteSlice firstQuoteSlice;
	protected Position position;
	protected StrategyBase strategyBase;
	
	public AlgorithmBase(boolean canTrade, Exchange exchange, Symbol symbol, AlgorithmMode algorithmMode){
		this.algorithmState.canTrade = canTrade;
		this.exchange = exchange;
		this.symbol = symbol;
		this.algorithmMode = algorithmMode;
		
		indicatorGroup = new IndicatorGroup(periodLength, commonAnlaysisData);
		signalGroup = new SignalGroup(indicatorGroup);
		
		if (algorithmMode.displayChart) {
			algorithmChart = new AlgorithmChart(symbol.symbolName, signalGroup, strategyBase.strategyOptions);
		}
		
		if (algorithmMode.displayTable){
			algorithmTable = new AlgorithmTable(symbol);
		}
	}
	
	public void setAlgorithmListener(AlgorithmListener algorithmListener){
		this.algorithmListener = algorithmListener;
	}
	
	public ReceiverOfQuoteSlice getReceiver(){
		return (ReceiverOfQuoteSlice) this;
	}
	
	public void handlePositionChange(boolean isReentry, Position position){
		if (isReentry == false){
			algorithmState.transactions++;
		}
	}
	
	public void handleStrategyResponse(StrategyResponse strategyResponse){
		if (strategyResponse.strategyAction == StrategyAction.algorithm_disable){
			if (algorithmState.isDisabled == false){
				disable();
				if (algorithmListener != null){
					algorithmListener.receiveChangedStrategyResponse(strategyResponse);
				}
				listOfStrategyResponse.add(strategyResponse);
			}
		}else if (strategyResponse.strategyAction == StrategyAction.algorithm_changed){
			PositionGovernorResponse positionGovernorResponse = strategyResponse.positionGovernorResponse;
			if (positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry
				|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit
				|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry
				|| positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
					handlePositionChange(false, strategyResponse.positionGovernorResponse.position);
			}else if (positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry || positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry){
				handlePositionChange(true, strategyResponse.positionGovernorResponse.position);
			}
			if (algorithmListener != null){
				algorithmListener.receiveChangedStrategyResponse(strategyResponse);
			}
			listOfStrategyResponse.add(strategyResponse);
		}else if (strategyResponse.strategyAction == StrategyAction.algorithm_proceed){
			algorithmListener.receiveStrategyResponse(strategyResponse);
		}
	}
	
	public void disable(){
		algorithmState.isDisabled = true;
	}
	
	public void receivedQuoteSlice(QuoteSlice quoteSlice){
		if (algorithmMode.displayMessages) {
			Co.println("Received quote: " + quoteSlice.symbol + ", " + DateTools.getPrettyDate(quoteSlice.dateTime) + ", " + "O,H,L,C,V: " + +MathTools.round(quoteSlice.priceOpen) + ", " + MathTools.round(quoteSlice.priceHigh) + ", " + MathTools.round(quoteSlice.priceLow) + ", " + MathTools.round(quoteSlice.priceClose) + ", " + quoteSlice.sizeVolume);
		}
		
		if (firstQuoteSlice == null){
			firstQuoteSlice = quoteSlice;
		}
		
		listOfQuoteSlice.add(quoteSlice);
		
		position = PositionManager.getInstance().getPosition(quoteSlice.symbol);
		
		PositionManager.getInstance().updatePositionPrice(quoteSlice, position);
	}
	
	public void finishedReceiverOfQuoteSlice(){
		if (listOfQuoteSlice.size() >= periodLength) {
			listOfQuoteSlice.remove(0);
		}
	}
	
	public QuoteSlice getCurrentQuoteSlice(){
		return listOfQuoteSlice.size() == 0 ? null : listOfQuoteSlice.get(listOfQuoteSlice.size()-1);
	}
	
	public QuoteSlice getFirstQuoteSlice(){
		return firstQuoteSlice;
	}

	@Override
	public void positionStatusChanged(Position position) {
//		Co.println("--> Received position change! ");
		if (position.positionType == PositionType.position_cancelled){
			Co.println("--> Position was cancelled... Disabling: " + position.symbol.symbolName);
			disable();
		}
	}
}
