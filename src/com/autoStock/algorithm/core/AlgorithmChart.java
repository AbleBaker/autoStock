package com.autoStock.algorithm.core;

import com.autoStock.chart.ChartForAlgorithmTest;
import com.autoStock.position.PositionGovernorResponseStatus;
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
	private SignalGroup signalGroup;
	
	public AlgorithmChart(String title, SignalGroup signalGroup){
		chart = new ChartForAlgorithmTest(title);
		this.signalGroup = signalGroup;
	}
	
	public void addChartPointData(QuoteSlice firstQuoteSlice, QuoteSlice quoteSlice, StrategyResponse strategyResponse){
		chart.listOfDate.add(quoteSlice.dateTime);
		chart.listOfPriceOpen.add(quoteSlice.priceOpen);
		chart.listOfPriceHigh.add(quoteSlice.priceHigh);
		chart.listOfPriceLow.add(quoteSlice.priceLow);
		chart.listOfPriceClose.add(quoteSlice.priceClose);
		chart.listOfSizeVolume.add((double)quoteSlice.sizeVolume);
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
		
		if (signalGroup != null){
			chart.listOfDebugAlpha.add(signalGroup.getIndicatorGroup().candleStickIdentifierResult.getLastValue() == 0 ? Double.MIN_VALUE : signalGroup.getIndicatorGroup().candleStickIdentifierResult.getLastValue());
		}
		
		if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry){
			chart.listOfEntryAtPrice.add(quoteSlice.priceClose);
			chart.listOfExitAtPrice.add(Double.MIN_VALUE);
			chart.listOfEntryAtSignal.add(SignalTools.getCombinedSignal(strategyResponse.signal).strength);
			chart.listOfExitAtSignal.add(Double.MIN_VALUE);
		}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit){
			chart.listOfEntryAtPrice.add(Double.MIN_VALUE);
			chart.listOfExitAtPrice.add(quoteSlice.priceClose);
			chart.listOfEntryAtSignal.add(Double.MIN_VALUE);
			chart.listOfExitAtSignal.add(SignalTools.getCombinedSignal(strategyResponse.signal).strength);
		}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry){
			chart.listOfEntryAtPrice.add(quoteSlice.priceClose);
			chart.listOfExitAtPrice.add(Double.MIN_VALUE);
			chart.listOfEntryAtSignal.add(SignalTools.getCombinedSignal(strategyResponse.signal).strength);
			chart.listOfExitAtSignal.add(Double.MIN_VALUE);
		}else{
			chart.listOfEntryAtPrice.add(Double.MIN_VALUE);
			chart.listOfExitAtPrice.add(Double.MIN_VALUE);
			chart.listOfEntryAtSignal.add(Double.MIN_VALUE);
			chart.listOfExitAtSignal.add(Double.MIN_VALUE);
		}
	}
	
	public void display(){
		chart.display();
	}
}
