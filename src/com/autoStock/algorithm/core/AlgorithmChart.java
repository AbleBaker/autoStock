package com.autoStock.algorithm.core;

import com.autoStock.chart.ChartForAlgorithmTest;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalGroup;
import com.autoStock.signal.SignalTools;
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
	
	public void addChartPointData(QuoteSlice quoteSlice, Signal signal, SignalGroup signalGroup){
		chart.listOfDate.add(quoteSlice.dateTime);
		chart.listOfPrice.add(quoteSlice.priceClose);
		chart.listOfSignalDI.add(signalGroup.signalOfDI.getSignal().strength);
		chart.listOfSignalCCI.add(signalGroup.signalOfCCI.getSignal().strength);
		chart.listOfSignalPPC.add(signalGroup.signalOfPPC.getSignal().strength);
		chart.listOfSignalMACD.add(signalGroup.signalOfMACD.getSignal().strength);
		chart.listOfSignalRSI.add(signalGroup.signalOfRSI.getSignal().strength);
		chart.listOfSignalTRIX.add(signalGroup.signalOfTRIX.getSignal().strength);
		chart.listOfSignalMFI.add(signalGroup.signalOfMFI.getSignal().strength);
		chart.listOfSignalROC.add(signalGroup.signalOfROC.getSignal().strength);
		chart.listOfSignalWILLR.add(signalGroup.signalOfWILLR.getSignal().strength);
		chart.listOfSignalTotal.add((int) SignalTools.getCombinedSignal(signal).strength);
	}
	
	public void display(){
		chart.display();
	}
}
