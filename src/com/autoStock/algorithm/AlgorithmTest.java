package com.autoStock.algorithm;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.Co;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.analysis.AnalysisADX;
import com.autoStock.analysis.AnalysisBB;
import com.autoStock.analysis.AnalysisCCI;
import com.autoStock.analysis.AnalysisMACD;
import com.autoStock.analysis.AnalysisRSI;
import com.autoStock.analysis.AnalysisSTORSI;
import com.autoStock.analysis.results.ResultsADX;
import com.autoStock.analysis.results.ResultsBB;
import com.autoStock.analysis.results.ResultsCCI;
import com.autoStock.analysis.results.ResultsMACD;
import com.autoStock.analysis.results.ResultsRSI;
import com.autoStock.analysis.results.ResultsSTORSI;
import com.autoStock.balance.Account;
import com.autoStock.chart.ChartForAlgorithmTest;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.signal.SignalOfADX;
import com.autoStock.signal.SignalOfCCI;
import com.autoStock.signal.SignalOfMACD;
import com.autoStock.signal.SignalOfPPC;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DataExtractor;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.StringTools;
import com.autoStock.types.TypeQuoteSlice;
import com.tictactec.ta.lib.MAType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmTest extends AlgorithmBase implements ReceiverOfQuoteSlice {
	
	public AlgorithmTest(boolean canTrade) {
		super(canTrade);
	}

	private int periodLength = 30;
	private int periodWindow = 10;
	public Benchmark bench = new Benchmark();
	
	private AnalysisCCI analysisOfCCI = new AnalysisCCI(periodLength, false);
	private AnalysisADX analysisOfADX = new AnalysisADX(periodLength, false);
	private AnalysisMACD analysisOfMACD = new AnalysisMACD(periodLength, false);
	private AnalysisBB analysisOfBB = new AnalysisBB(periodLength, false);
	private AnalysisSTORSI analysisOfSTORSI = new AnalysisSTORSI(periodLength, false);
	private AnalysisRSI analysisOfRSI = new AnalysisRSI(periodLength, false); 
	
	private ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
	private ArrayList<TypeQuoteSlice> listOfQuoteSlice = new ArrayList<TypeQuoteSlice>();
	private Signal signal = new Signal(SignalSource.from_analysis);
	private ChartForAlgorithmTest chart = new ChartForAlgorithmTest();
	private PositionManager positionManager = PositionManager.instance;

	@Override
	public void receiveQuoteSlice(TypeQuoteSlice typeQuoteSlice) {
		//Co.println("Received backtest quote: " + DateTools.getPrettyDate(typeQuoteSlice.dateTime) + ", " + typeQuoteSlice.priceClose);
		
		listOfQuoteSlice.add(typeQuoteSlice);
	
		if (listOfQuoteSlice.size() > (periodLength + periodWindow)){
			double analysisPrice = typeQuoteSlice.priceClose;
			
			if (listOfQuoteSlice.size() > (periodLength + periodWindow)){
				listOfQuoteSlice.remove(0);
			}
			
			analysisOfCCI.setDataSet(listOfQuoteSlice);
			analysisOfADX.setDataSet(listOfQuoteSlice);
			analysisOfBB.setDataSet(listOfQuoteSlice);
			analysisOfMACD.setDataSet(listOfQuoteSlice);
			analysisOfSTORSI.setDataSet(listOfQuoteSlice);
			analysisOfRSI.setDataSet(listOfQuoteSlice);
			
			ResultsCCI resultsCCI = analysisOfCCI.analyize();
			ResultsADX resultsADX = analysisOfADX.analize();
			ResultsBB resultsBB = analysisOfBB.analyize(MAType.Ema);
			ResultsMACD resultsMACD = analysisOfMACD.analize();
			ResultsSTORSI resultsSTORSI = analysisOfSTORSI.analize();
			ResultsRSI resultsRSI = analysisOfRSI.analize();
			
			double analysisOfCCIResult = resultsCCI.arrayOfCCI[periodWindow-1];
			double analysisOfADXResult =  resultsADX.arrayOfADX[periodWindow-1];
			double analysisOfBBResultUpper =  resultsBB.arrayOfUpperBand[periodWindow-1];
			double analysisOfBBResultLower =  resultsBB.arrayOfLowerBand[periodWindow-1];
			double analysisOfMACDResult =  resultsMACD.arrayOfMACD[periodWindow-1]*1000;
			double analysisOfSTORSIResultK =  resultsSTORSI.arrayOfPercentK[periodWindow-1];
			double analysisOfSTORSIResultD =  resultsSTORSI.arrayOfPercentD[periodWindow-1];
			double analysisOfRSIResult = resultsRSI.arrayOfRSI[periodWindow-1];
			
			double[] arrayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)listOfQuoteSlice), "priceClose").toArray(new Double[0]));
			
			SignalOfPPC signalOfPPC = new SignalOfPPC(ArrayTools.subArray(arrayOfPriceClose, periodLength, periodLength + periodWindow), SignalControl.periodAverageForPPC);
			SignalOfADX signalOfADX = new SignalOfADX(ArrayTools.subArray(resultsADX.arrayOfADX, 0, periodWindow), SignalControl.periodAverageForADX);
			SignalOfCCI signalOfCCI = new SignalOfCCI(ArrayTools.subArray(resultsCCI.arrayOfCCI, 0, periodWindow), SignalControl.periodAverageForCCI);
			SignalOfMACD signalOfMACD = new SignalOfMACD(ArrayTools.subArray(resultsMACD.arrayOfMACD, 0, periodWindow), SignalControl.periodAverageForMACD);
			
			signal.reset();
			signal.addSignalMetrics(signalOfPPC.getSignal(), signalOfADX.getSignal(), signalOfCCI.getSignal(), signalOfMACD.getSignal());
			
			chart.listOfDate.add(typeQuoteSlice.dateTime);
			chart.listOfPrice.add(typeQuoteSlice.priceClose);
			chart.listOfSignalADX.add(signalOfADX.getSignal().strength);
			chart.listOfSignalCCI.add(signalOfCCI.getSignal().strength);
			chart.listOfSignalPPC.add(signalOfPPC.getSignal().strength);
			chart.listOfSignalMACD.add(signalOfMACD.getSignal().strength);
			
			chart.listOfADX.add(analysisOfADXResult);
			chart.listOfCCI.add(analysisOfCCIResult);
			chart.listOfMACD.add(analysisOfMACDResult);
			chart.listOfRSI.add(analysisOfRSIResult);
			
			if (algorithmListener != null){
				algorithmListener.recieveSignal(signal, typeQuoteSlice);
			}
			
			ArrayList<String> columnValues = new ArrayList<String>();
			
			columnValues.add(DateTools.getPrettyDate(typeQuoteSlice.dateTime));
			columnValues.add(String.valueOf(typeQuoteSlice.priceClose));
			columnValues.add(String.valueOf(StringTools.addPlusToPositiveNumbers(MathTools.roundToTwoDecimalPlaces(typeQuoteSlice.priceClose - listOfQuoteSlice.get(listOfQuoteSlice.size()-2).priceClose))));
			columnValues.add(String.valueOf(signalOfMACD.getValue()));
			columnValues.add(String.valueOf(signalOfMACD.getSignal().strength + "," + signalOfMACD.getSignal().signalTypeMetric.name()));
			columnValues.add(String.valueOf(signalOfPPC.getValue()));
			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfADXResult)));
			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfCCIResult)));
			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfBBResultUpper)));
			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfBBResultLower)));
			columnValues.add(String.valueOf(analysisOfMACDResult));
			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfSTORSIResultK)));
			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfSTORSIResultD)));
			
//			columnValues.add(DateTools.getPrettyDate(typeQuoteSlice.dateTime));
//			columnValues.add(String.valueOf(typeQuoteSlice.priceClose));
//			columnValues.add(String.valueOf(StringTools.addPlusToPositiveNumbers(MathTools.roundToTwoDecimalPlaces(typeQuoteSlice.priceClose - listOfQuoteSlice.get(listOfQuoteSlice.size()-2).priceClose))));
//			columnValues.add(String.valueOf(signalOfPPC.getSignal().strength));
//			columnValues.add(String.valueOf(signalOfADX.getSignal().strength));
//			columnValues.add(String.valueOf(signalOfCCI.getSignal().strength));
//			columnValues.add(String.valueOf(signalOfMACD.getSignal().strength));
//			columnValues.add(String.valueOf(signal.getCombinedSignal()));
			
			if (signal.getCombinedSignal() > 50){
				signal.currentSignalType = SignalType.type_buy;
				positionManager.suggestPosition(typeQuoteSlice, signal);
			}else if (signal.getCombinedSignal() < -75){
				signal.currentSignalType = SignalType.type_sell;
				positionManager.suggestPosition(typeQuoteSlice, signal);
//			}else if (signal.getCombinedSignal() < -76){
//				signal.currentSignalType = SignalType.type_short;
//				positionManager.suggestPosition(typeQuoteSlice, signal);
//			}else {
				signal.currentSignalType = SignalType.type_none;
			}
			
			listOfDisplayRows.add(columnValues);
			
		}else{
			//Co.println("Waiting for more data (period condition)... ");
		}
	}

	@Override
	public void endOfFeed() {
		bench.total();
		if (algorithmListener != null){
			algorithmListener.endOfAlgorithm();
		}
		//PositionManager.instance.induceSellAll();
		//Co.println("Account balance: " + AccountBalance.instance.getBankBalance() + " Fees paid: " + AccountBalance.instance.getTransactionFeesPaid());
		//chart.display();
		new TableController().displayTable(AsciiTables.analysis_test, listOfDisplayRows);
		//new TableController().displayTable(AsciiTables.algorithm_test, listOfDisplayRows);
	}
}
