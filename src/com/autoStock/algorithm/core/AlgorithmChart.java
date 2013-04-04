package com.autoStock.algorithm.core;

import com.autoStock.Co;
import com.autoStock.chart.ChartForAlgorithmTest;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.signal.SignalGroup;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * 
 */
public class AlgorithmChart {
	private ChartForAlgorithmTest chart;
	private SignalGroup signalGroup;

	public AlgorithmChart(String title, SignalGroup signalGroup, StrategyOptions strategyOptions) {
		chart = new ChartForAlgorithmTest(title);
		chart.strategyOptions = strategyOptions;
		this.signalGroup = signalGroup;
	}

	public void addChartPointData(QuoteSlice firstQuoteSlice, QuoteSlice quoteSlice, StrategyResponse strategyResponse) {
		chart.listOfDate.add(quoteSlice.dateTime);
		chart.listOfPriceOpen.add(quoteSlice.priceOpen);
		chart.listOfPriceHigh.add(quoteSlice.priceHigh);
		chart.listOfPriceLow.add(quoteSlice.priceLow);
		chart.listOfPriceClose.add(quoteSlice.priceClose);
		chart.listOfSizeVolume.add((double) quoteSlice.sizeVolume);
		chart.listOfSignalADX.add(signalGroup.signalOfADX.getStrength());
		chart.listOfSignalDI.add(signalGroup.signalOfDI.getStrength());
		chart.listOfSignalCCI.add(signalGroup.signalOfCCI.getStrength());
		chart.listOfSignalMACD.add(signalGroup.signalOfMACD.getStrength());
		chart.listOfSignalRSI.add(signalGroup.signalOfRSI.getStrength());
		chart.listOfSignalTRIX.add(signalGroup.signalOfTRIX.getStrength());
		chart.listOfSignalMFI.add(signalGroup.signalOfMFI.getStrength());
		chart.listOfSignalROC.add(signalGroup.signalOfROC.getStrength());
		chart.listOfSignalWILLR.add(signalGroup.signalOfWILLR.getStrength());
		chart.listOfValue.add(strategyResponse.positionGovernorResponse.position == null ? Double.MIN_VALUE : strategyResponse.positionGovernorResponse.position.getCurrentPercentGainLoss(false));

		if (signalGroup != null) {
			chart.listOfDebugAlpha.add(signalGroup.getIndicatorGroup().candleStickIdentifierResult.getLastValue() == 0 ? Double.MIN_VALUE : signalGroup.getIndicatorGroup().candleStickIdentifierResult.getLastValue());
		}

		chart.listOfLongEntryAtPrice.add(Double.MIN_VALUE);
		chart.listOfShortEntryAtPrice.add(Double.MIN_VALUE);
		chart.listOfReEntryAtPrice.add(Double.MIN_VALUE);
		chart.listOfLongExitAtPrice.add(Double.MIN_VALUE);
		chart.listOfShortExitAtPrice.add(Double.MIN_VALUE);
		chart.listOfEntryAtSignal.add(Double.MIN_VALUE);
		chart.listOfExitAtSignal.add(Double.MIN_VALUE);

		if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry) {
			chart.listOfLongEntryAtPrice.remove(chart.listOfLongEntryAtPrice.size()-1);
			chart.listOfLongEntryAtPrice.add(quoteSlice.priceClose);
		} else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry) {
			chart.listOfShortEntryAtPrice.remove(chart.listOfShortEntryAtPrice.size()-1);
			chart.listOfShortEntryAtPrice.add(quoteSlice.priceClose);
		} else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit) {
			chart.listOfLongExitAtPrice.remove(chart.listOfLongExitAtPrice.size()-1);
			chart.listOfLongExitAtPrice.add(quoteSlice.priceClose);
		} else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit) {
			chart.listOfShortExitAtPrice.remove(chart.listOfShortExitAtPrice.size()-1);
			chart.listOfShortExitAtPrice.add(quoteSlice.priceClose);
		} else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry) {
			chart.listOfReEntryAtPrice.add(quoteSlice.priceClose);
		} else {
			// pass
		}
	}

	public void display() {
		chart.display();
	}
}
