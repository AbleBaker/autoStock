package com.autoStock.algorithm;

import java.util.ArrayList;
import java.util.Arrays;

import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentOfSignalMetric;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.strategy.StrategyHelper;
import com.autoStock.strategy.StrategyOfTest;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.Benchmark;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class AlgorithmTest extends AlgorithmBase implements ReceiverOfQuoteSlice {
	public StrategyOfTest strategy = new StrategyOfTest(this);
	private ArrayList<SignalMetricType> listOfSignalMetricType = new ArrayList<SignalMetricType>();
	
	public AlgorithmTest(boolean canTrade, Exchange exchange, Symbol symbol, AlgorithmMode algorithmMode) {
		super(canTrade, exchange, symbol, algorithmMode);
		
		if (algorithmMode == AlgorithmMode.mode_backtest_with_adjustment){
//		if (false){
			for (AdjustmentBase adjustmentBase : AdjustmentCampaign.getInstance().getListOfAdjustmentBase()){
				if (adjustmentBase instanceof AdjustmentOfSignalMetric){
					listOfSignalMetricType.add(((AdjustmentOfSignalMetric)adjustmentBase).signalMetricType);
				}
			}
			
//			listOfSignalMetricType.add(SignalMetricType.metric_di);
		}else{
			listOfSignalMetricType.addAll(Arrays.asList(SignalMetricType.values()));	
		}
	}

	@Override
	public synchronized void receiveQuoteSlice(QuoteSlice quoteSlice) {
		receivedQuoteSlice(quoteSlice);
		
		if (listOfQuoteSlice.size() >= periodLength) {
			commonAnlaysisData.setAnalysisData(listOfQuoteSlice);
			indicatorGroup.setDataSet(listOfQuoteSlice, periodLength);
			indicatorGroup.analyize(listOfSignalMetricType);
			signalGroup.generateSignals(commonAnlaysisData, periodLength);

			StrategyResponse strategyResponse = strategy.informStrategy(indicatorGroup, signalGroup, listOfQuoteSlice, listOfStrategyResponse);
		
			handleStrategyResponse(strategyResponse);

			if (algorithmMode.displayChart) {
				algorithmChart.addChartPointData(firstQuoteSlice, quoteSlice, strategyResponse);
			}
			
			if (algorithmMode.displayTable) {
				algorithmTable.addTableRow(listOfQuoteSlice, strategy.signal, signalGroup, strategyResponse);
			}
			
			periodLength = StrategyHelper.getUpdatedPeriodLength(quoteSlice.dateTime, exchange, periodLength, strategy.strategyOptions);
			finishedReceiverOfQuoteSlice();
			
//			bench.printTick("Done received");
		}
	}

	@Override
	public synchronized void endOfFeed(Symbol symbol) {
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
