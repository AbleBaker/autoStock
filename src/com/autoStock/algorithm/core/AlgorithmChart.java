package com.autoStock.algorithm.core;

import com.autoStock.chart.ChartForAlgorithmTest;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseStatus;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalGroup;
import com.autoStock.signal.SignalTools;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmChart {
	private ChartForAlgorithmTest chart;
	
	public AlgorithmChart(String title){
		chart = new ChartForAlgorithmTest(title);
	}
	
	public void addChartPointData(QuoteSlice firstQuoteSlice, QuoteSlice quoteSlice, SignalGroup signalGroup, StrategyResponse strategyResponse){
		chart.listOfDate.add(quoteSlice.dateTime);
		chart.listOfPrice.add(quoteSlice.priceClose);
		chart.listOfSignalDI.add(signalGroup.signalOfDI.getSignal().getStrength());
		chart.listOfSignalCCI.add(signalGroup.signalOfCCI.getSignal().getStrength());
		chart.listOfSignalPPC.add(signalGroup.signalOfPPC.getSignal().getStrength());
		chart.listOfSignalMACD.add(signalGroup.signalOfMACD.getSignal().getStrength());
		chart.listOfSignalRSI.add(signalGroup.signalOfRSI.getSignal().getStrength());
		chart.listOfSignalTRIX.add(signalGroup.signalOfTRIX.getSignal().getStrength());
		chart.listOfSignalMFI.add(signalGroup.signalOfMFI.getSignal().getStrength());
		chart.listOfSignalROC.add(signalGroup.signalOfROC.getSignal().getStrength());
		chart.listOfSignalWILLR.add(signalGroup.signalOfWILLR.getSignal().getStrength());
		chart.listOfSignalTotal.add((int) SignalTools.getCombinedSignal(strategyResponse.signal).strength);
		chart.listOfValue.add(strategyResponse.positionGovernorResponse.position == null ? Double.MIN_VALUE : strategyResponse.positionGovernorResponse.position.getCurrentPercentGainLoss(false));
		//(quoteSlice.priceClose / firstQuoteSlice.priceClose)); 
		
		if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry){
			chart.listOfEntry.add(quoteSlice.priceClose);
			chart.listOfExit.add(Double.MIN_VALUE);
		}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit){
			chart.listOfEntry.add(Double.MIN_VALUE);
			chart.listOfExit.add(quoteSlice.priceClose);
		}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry){
			chart.listOfEntry.add(quoteSlice.priceClose);
			chart.listOfExit.add(Double.MIN_VALUE);
		}else{
			chart.listOfEntry.add(Double.MIN_VALUE);
			chart.listOfExit.add(Double.MIN_VALUE);
		}
	}
	
	public void display(){
		chart.display();
	}
}
