package com.autoStock.algorithm.core;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.chart.ChartForAlgorithmTest;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.ArrayTools;
import com.autoStock.types.QuoteSlice;
import com.google.gson.Gson;

/**
 * @author Kevin Kowalewski
 * 
 */
public class AlgorithmChart {
	private ChartForAlgorithmTest chart;
	private AlgorithmBase algorithmBase;

	public AlgorithmChart(String title, AlgorithmBase algorithmBase) {
		this.algorithmBase = algorithmBase;
		chart = new ChartForAlgorithmTest(title, algorithmBase);
		chart.strategyOptions = algorithmBase.strategyBase.strategyOptions;
	}

	public void addChartPointData(QuoteSlice firstQuoteSlice, QuoteSlice quoteSlice, StrategyResponse strategyResponse) {
		chart.listOfDate.add(quoteSlice.dateTime);
		chart.listOfPriceOpen.add(quoteSlice.priceOpen);
		chart.listOfPriceHigh.add(quoteSlice.priceHigh);
		chart.listOfPriceLow.add(quoteSlice.priceLow);
		chart.listOfPriceClose.add(quoteSlice.priceClose);
		chart.listOfSizeVolume.add((double) quoteSlice.sizeVolume);
		chart.listOfSignalADX.add(algorithmBase.signalGroup.signalOfADX.getStrength());
		chart.listOfSignalDI.add(algorithmBase.signalGroup.signalOfDI.getStrength());
		chart.listOfSignalCCI.add(algorithmBase.signalGroup.signalOfCCI.getStrength());
		chart.listOfSignalMACD.add(algorithmBase.signalGroup.signalOfMACD.getStrength());
		chart.listOfSignalRSI.add(algorithmBase.signalGroup.signalOfRSI.getStrength());
		chart.listOfSignalTRIX.add(algorithmBase.signalGroup.signalOfTRIX.getStrength());
		chart.listOfSignalMFI.add(algorithmBase.signalGroup.signalOfMFI.getStrength());
		chart.listOfSignalROC.add(algorithmBase.signalGroup.signalOfROC.getStrength());
		chart.listOfSignalWILLR.add(algorithmBase.signalGroup.signalOfWILLR.getStrength());
		chart.listOfSignalUO.add(algorithmBase.signalGroup.signalOfUO.getStrength());
		chart.listOfSignalARUp.add(algorithmBase.signalGroup.signalOfARUp.getStrength());
		chart.listOfSignalARDown.add(algorithmBase.signalGroup.signalOfARDown.getStrength());
		chart.listOfSignalSAR.add(algorithmBase.signalGroup.signalOfSAR.getStrength());
		
		chart.listOfIndicatorEMAFirst.add(ArrayTools.getLastElement(algorithmBase.indicatorGroup.resultsEMAFirst.arrayOfEMA));
		chart.listOfIndicatorEMASecond.add(ArrayTools.getLastElement(algorithmBase.indicatorGroup.resultsEMASecond.arrayOfEMA));
		
		chart.listOfSignalSAR.add(algorithmBase.signalGroup.signalOfSAR.getStrength());
		chart.listOfIndicatorSAR.add(ArrayTools.getLastElement(algorithmBase.indicatorGroup.resultsSAR.arrayOfSAR));
		
		chart.listOfValue.add(strategyResponse.positionGovernorResponse.position == null ? Double.MIN_VALUE : strategyResponse.positionGovernorResponse.position.getCurrentPercentGainLoss(false));
		chart.listOfYield.add(algorithmBase.getYieldCurrent());

		if (algorithmBase.signalGroup != null && algorithmBase.indicatorGroup.resultsPTD != null) {
			if (algorithmBase.indicatorGroup.resultsPTD.arrayOfPTD[0] == 1){
				chart.listOfDebugAlpha.add((double)algorithmBase.signalGroup.signalOfUO.getStrength());	
			}else{
				chart.listOfDebugAlpha.add(Double.MIN_VALUE);
			}
			 //algorithmBase.signalGroup.getIndicatorGroup().candleStickIdentifierResult.getLastValue() == 0 ? Double.MIN_VALUE : algorithmBase.signalGroup.getIndicatorGroup().candleStickIdentifierResult.getLastValue());
		}else{
			chart.listOfDebugAlpha.add(0d);
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
			chart.listOfReEntryAtPrice.remove(chart.listOfReEntryAtPrice.size()-1);
			chart.listOfReEntryAtPrice.add(quoteSlice.priceClose);
		} else {
			// pass
		}
	}

	public void display() {
//		Co.print(new Gson().toJson(chart.listOfSignalUO));
		chart.display();
	}
}
