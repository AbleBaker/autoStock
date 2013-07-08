package com.autoStock.algorithm;

import java.util.Arrays;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.account.BasicAccount;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.position.PositionOptions;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.strategy.StrategyOfTest;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class AlgorithmTest extends AlgorithmBase {
	public StrategyOfTest strategy = new StrategyOfTest(this);
	
	public AlgorithmTest(Exchange exchange, Symbol symbol, AlgorithmMode algorithmMode, BasicAccount basicAccount) {
		super(exchange, symbol, algorithmMode, basicAccount);
	}
	
	public void init(Date startingDate){
		this.startingDate = startingDate;
		
		if (algorithmMode == AlgorithmMode.mode_backtest_with_adjustment){
			listOfSignalMetricType = strategy.strategyOptions.listOfSignalMetricType;
		}else{
			listOfSignalMetricType.addAll(Arrays.asList(SignalMetricType.values()));	
		}
	
		initialize(strategy);
		if (strategy.strategyOptions.prefillEnabled){
			prefill();			
		}
	}

	@Override
	public void receiveQuoteSlice(QuoteSlice quoteSlice) {
		receivedQuoteSlice(quoteSlice);
			
		if (listOfQuoteSlice.size() >= getPeriodLength()) {
//			Co.print("\n --> QS: " + quoteSlice.dateTime);
			
			commonAnalysisData.setAnalysisData(listOfQuoteSlice);
			indicatorGroup.setDataSet();
			indicatorGroup.analyize();
			signalGroup.setIndicatorGroup(indicatorGroup);
			signalGroup.generateSignals(commonAnalysisData, getPeriodLength());
//			
//			for (int i=0; i<indicatorGroup.indicatorOfRSI.results.arrayOfRSI.length; i++){
//				Co.print(" " + indicatorGroup.indicatorOfRSI.results.arrayOfRSI[i]);
//			}
//			
//			System.exit(0);
			
			StrategyResponse strategyResponse = strategy.informStrategy(indicatorGroup, signalGroup, listOfQuoteSlice, listOfStrategyResponse, position, new PositionOptions(this));
		
			handleStrategyResponse(strategyResponse);

			if (algorithmMode.displayChart) {
				algorithmChart.addChartPointData(firstQuoteSlice, quoteSlice, strategyResponse);
			}
			
			if (algorithmMode.displayTable) {
				algorithmTable.addTableRow(listOfQuoteSlice, strategy.signal, signalGroup, strategyResponse, basicAccount);
			}
			
//			periodLength = StrategyHelper.getUpdatedPeriodLength(quoteSlice.dateTime, exchange, periodLength, strategy.strategyOptions);
			finishedReceiverOfQuoteSlice();
		}
	}

	@Override
	public void endOfFeed(Symbol symbol) {
		if (algorithmMode.displayChart) {
			algorithmChart.display();
		}
		if (algorithmMode.displayTable) {
			algorithmTable.display();
		}
		if (algorithmListener != null) {
			algorithmListener.endOfAlgorithm();
		}
	}
}
