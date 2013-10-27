/**
 * 
 */
package com.autoStock.algorithm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jfree.data.time.Year;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.account.BasicAccount;
import com.autoStock.algorithm.core.AlgorithmChart;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.core.AlgorithmListener;
import com.autoStock.algorithm.core.AlgorithmRemodeler;
import com.autoStock.algorithm.core.AlgorithmState;
import com.autoStock.algorithm.core.AlgorithmTable;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.indicator.CommonAnalysisData;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.position.ListenerOfPositionStatusChange;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.position.PositionManager;
import com.autoStock.retrospect.Prefill;
import com.autoStock.retrospect.Prefill.PrefillMethod;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalGroup;
import com.autoStock.strategy.StrategyBase;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.strategy.StrategyResponse.StrategyAction;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.types.Position;
import com.autoStock.trading.yahoo.FundamentalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class AlgorithmBase implements ListenerOfPositionStatusChange, ReceiverOfQuoteSlice {
	private int periodLength;
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
	public CommonAnalysisData commonAnalysisData = new CommonAnalysisData();
	public final ArrayList<QuoteSlice> listOfQuoteSlice = new ArrayList<QuoteSlice>();
	public final ArrayList<StrategyResponse> listOfStrategyResponse = new ArrayList<StrategyResponse>();
	protected ArrayList<SignalMetricType> listOfSignalMetricType = new ArrayList<SignalMetricType>();
	public QuoteSlice firstQuoteSlice;
	protected Position position;
	public StrategyBase strategyBase;
	private Prefill prefill;
	public Date startingDate;
	protected FundamentalData fundamentalData;
	public final BasicAccount basicAccount;
	public Double dayStartingBalance;
	
	public AlgorithmBase(Exchange exchange, Symbol symbol, AlgorithmMode algorithmMode, BasicAccount basicAccount){
		this.exchange = exchange;
		this.symbol = symbol;
		this.algorithmMode = algorithmMode;
		this.basicAccount = basicAccount;
		
		signalGroup = new SignalGroup();
		indicatorGroup = new IndicatorGroup(commonAnalysisData, signalGroup);
	}
	

	@Override
	public abstract void receiveQuoteSlice(QuoteSlice quoteSlice);

	@Override
	public abstract void endOfFeed(Symbol symbol);
	
	public void initialize(){
		if (algorithmMode.displayChart) {
			algorithmChart = new AlgorithmChart(symbol.symbolName + " - " + new SimpleDateFormat("EEE MMM dd yyyy").format(startingDate), this);
		}
		
		if (algorithmMode.displayTable){
			algorithmTable = new AlgorithmTable(symbol);
		}
		
		if (algorithmMode == AlgorithmMode.mode_backtest_with_adjustment){
			//Check Strategy actually contains adjustment values... 
		}
		
		indicatorGroup.setActive(listOfSignalMetricType);
		periodLength = indicatorGroup.getMinPeriodLength(algorithmMode != AlgorithmMode.mode_backtest_single);
		
		if (PositionManager.getInstance().getPositionListSize() != 0){
			PositionManager pm = PositionManager.getInstance();
			throw new IllegalStateException();
		}
		
		dayStartingBalance = basicAccount.getBalance();
		
		listOfQuoteSlice.clear();
		listOfStrategyResponse.clear();
		commonAnalysisData.reset();
		signalGroup.reset();
		algorithmState.reset();
		
		PGResponsePrevious = null;
	}
	
	public void setAlgorithmListener(AlgorithmListener algorithmListener){
		this.algorithmListener = algorithmListener;
	}
	
	public ReceiverOfQuoteSlice getReceiver(){
		return this;
	}
	
	public void handlePositionChange(boolean isReentry, Position position){
		if (isReentry == false){
			algorithmState.transactions++;
		}
	}
	
	public void handleStrategyResponse(StrategyResponse strategyResponse) {
		if (strategyResponse.strategyAction == StrategyAction.algorithm_disable){
			if (algorithmState.isDisabled == false){
				disable(strategyResponse.strategyActionCause.name());
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
		}else if (strategyResponse.strategyAction== StrategyAction.algorithm_pass){
			listOfStrategyResponse.add(strategyResponse);
		}
	}
	
	public void disable(String reason){
		algorithmState.isDisabled = true;
		algorithmState.disabledReason = reason;
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
	
	protected void prefill(){
		if (algorithmMode == AlgorithmMode.mode_backtest || algorithmMode == AlgorithmMode.mode_backtest_with_adjustment || algorithmMode == AlgorithmMode.mode_backtest_silent){
			prefill = new Prefill(symbol, exchange, PrefillMethod.method_database);
		} else if(algorithmMode == AlgorithmMode.mode_engagement){
			prefill = new Prefill(symbol, exchange, PrefillMethod.method_broker);
		}
		
		prefill.prefillAlgorithm(this, strategyBase.strategyOptions);
		
		if (listOfQuoteSlice.size() > 0){
			firstQuoteSlice = listOfQuoteSlice.get(0);
		}
	}
	
	public void remodel(BacktestEvaluation backtestEvaluation){
		new AlgorithmRemodeler(this, backtestEvaluation.algorithmModel).remodel();
	}
	
	public void remodel(BacktestEvaluation backtestEvaluation, boolean includeStrategyOptions, boolean includeSignalParameters, boolean includeIndicatorParameters){
		new AlgorithmRemodeler(this, backtestEvaluation.algorithmModel).remodel(includeStrategyOptions, includeSignalParameters, includeIndicatorParameters);
	}
	
	public void setFundamentalData(FundamentalData fundamentalData){
		this.fundamentalData = fundamentalData;
	}
	
	public int getPeriodLength(){
		return periodLength;
	}
	
	protected void setStrategy(StrategyBase strategyBase){
		this.strategyBase = strategyBase;
	}

	@Override
	public void positionStatusChanged(Position position) {
//		Co.println("--> Received position change! ");
		if (position.positionType == PositionType.position_cancelled){
			Co.println("--> Position was cancelled... Disabling: " + position.symbol.symbolName);
			disable(position.positionType.name());
		}
	}
	
	public double getYieldCurrent(){
		return getCurrentYield(false);
	}
	
	public double getYieldComplete(){
		return getCurrentYield(true);
	}


	private synchronized double getCurrentYield(boolean complete) {	
		double positionCost = 0;
		Position currentPosition = null;
		
		for (StrategyResponse strategyResponse : listOfStrategyResponse){
			PositionGovernorResponse positionGovernorResponse = strategyResponse.positionGovernorResponse;
			
			if (positionGovernorResponse != null && positionGovernorResponse.position != null){
				positionCost = Math.max(positionCost, positionGovernorResponse.position.getPositionValue().priceCurrent);
				
				if (positionGovernorResponse.position.isFilledAndOpen()){
					currentPosition = positionGovernorResponse.position;
				}
			}
		}
		
		double yield = 0;
		
		if (currentPosition != null){
			double totalValue = currentPosition.getPositionValue().valueCurrentWithFees + basicAccount.getBalance();
			double increasedValue = totalValue - (complete == true ? AccountProvider.defaultBalance : dayStartingBalance);
			yield = (increasedValue / positionCost) * 100;
		}else if (positionCost != 0){
			double increasedValue = basicAccount.getBalance() - (complete == true ? AccountProvider.defaultBalance : dayStartingBalance);
			yield = (increasedValue / positionCost) * 100;
		}else{
			//pass, no positions!
		}
		
		if (yield > 25){
			Co.println("--> Have current position: " + currentPosition);
			Co.println("--> Position cost: " + positionCost);
			Co.println("--> Balance: " +  basicAccount.getBalance());
			Co.println("--> Starting Balance: " + dayStartingBalance);
			Co.println("--> Yield: " + yield);
			Co.println("--> Complete: " + complete);

			throw new IllegalStateException();
		}

		return yield;
	}
}
