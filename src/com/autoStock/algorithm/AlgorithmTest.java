package com.autoStock.algorithm;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.Co;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.analysis.AnalysisOfADX;
import com.autoStock.analysis.AnalysisOfBB;
import com.autoStock.analysis.AnalysisOfCCI;
import com.autoStock.analysis.AnalysisOfMACD;
import com.autoStock.analysis.AnalysisOfRSI;
import com.autoStock.analysis.AnalysisOfSTORSI;
import com.autoStock.analysis.AnalysisOfTRIX;
import com.autoStock.analysis.results.ResultsADX;
import com.autoStock.analysis.results.ResultsBB;
import com.autoStock.analysis.results.ResultsCCI;
import com.autoStock.analysis.results.ResultsMACD;
import com.autoStock.analysis.results.ResultsRSI;
import com.autoStock.analysis.results.ResultsSTORSI;
import com.autoStock.analysis.results.ResultsTRIX;
import com.autoStock.chart.ChartForAlgorithmTest;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionGovernor;
import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.signal.SignalOfADX;
import com.autoStock.signal.SignalOfCCI;
import com.autoStock.signal.SignalOfMACD;
import com.autoStock.signal.SignalOfPPC;
import com.autoStock.signal.SignalOfRSI;
import com.autoStock.signal.SignalOfSTORSI;
import com.autoStock.signal.SignalOfTRIX;
import com.autoStock.taLib.MAType;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DataExtractor;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.StringTools;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmTest extends AlgorithmBase implements ReceiverOfQuoteSlice {
	public AlgorithmTest(boolean canTrade) {
		super(canTrade);
	}

	private int periodLength = 60;
	private int periodWindow = 30;
	public Benchmark bench = new Benchmark();
	
	private AnalysisOfCCI analysisOfCCI = new AnalysisOfCCI(periodLength, false);
	private AnalysisOfADX analysisOfADX = new AnalysisOfADX(periodLength, false);
	private AnalysisOfMACD analysisOfMACD = new AnalysisOfMACD(periodLength, false);
	private AnalysisOfBB analysisOfBB = new AnalysisOfBB(periodLength, false);
	private AnalysisOfSTORSI analysisOfSTORSI = new AnalysisOfSTORSI(periodLength, false);
	private AnalysisOfRSI analysisOfRSI = new AnalysisOfRSI(periodLength, false);
	private AnalysisOfTRIX analysisOfTRIX = new AnalysisOfTRIX(periodLength, false);
	
	private ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
	private ArrayList<TypeQuoteSlice> listOfQuoteSlice = new ArrayList<TypeQuoteSlice>();
	private Signal signal = new Signal(SignalSource.from_analysis);
	private ChartForAlgorithmTest chart = new ChartForAlgorithmTest();
	private PositionGovernor positionGovener = new PositionGovernor();

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
			analysisOfTRIX.setDataSet(listOfQuoteSlice);
			
			ResultsCCI resultsCCI = analysisOfCCI.analyize();
			ResultsADX resultsADX = analysisOfADX.analize();
			ResultsBB resultsBB = analysisOfBB.analyize(MAType.Ema);
			ResultsMACD resultsMACD = analysisOfMACD.analize();
			ResultsSTORSI resultsSTORSI = analysisOfSTORSI.analyize();
			ResultsRSI resultsRSI = analysisOfRSI.analyize();
			ResultsTRIX resultsTRIX = analysisOfTRIX.analyize();
			
			double[] arrayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)listOfQuoteSlice), "priceClose").toArray(new Double[0]));
			double analysisOfCCIResult = resultsCCI.arrayOfCCI[periodWindow-1];
			double analysisOfADXResult = resultsADX.arrayOfADX[periodWindow-1];
			double analysisOfBBResultUpper = resultsBB.arrayOfUpperBand[periodWindow-1];
			double analysisOfBBResultLower = resultsBB.arrayOfLowerBand[periodWindow-1];
			double analysisOfMACDResult = resultsMACD.arrayOfMACDHistogram[periodWindow-1]*1000;
			double analysisOfSTORSIResultK = resultsSTORSI.arrayOfPercentK[periodWindow-1];
			double analysisOfSTORSIResultD = resultsSTORSI.arrayOfPercentD[periodWindow-1];
			double analysisOfRSIResult = resultsRSI.arrayOfRSI[periodWindow-1];
			double analysisOfTrixResult = resultsTRIX.arrayOfTRIX[periodWindow-1];
			
			SignalOfPPC signalOfPPC = new SignalOfPPC(ArrayTools.subArray(arrayOfPriceClose, periodLength, periodLength + periodWindow), SignalControl.periodAverageForPPC);
			SignalOfADX signalOfADX = new SignalOfADX(ArrayTools.subArray(resultsADX.arrayOfADX, 0, periodWindow), SignalControl.periodAverageForADX);
			SignalOfCCI signalOfCCI = new SignalOfCCI(ArrayTools.subArray(resultsCCI.arrayOfCCI, 0, periodWindow), SignalControl.periodAverageForCCI);
			SignalOfMACD signalOfMACD = new SignalOfMACD(ArrayTools.subArray(resultsMACD.arrayOfMACDHistogram, 0, periodWindow), SignalControl.periodAverageForMACD);
			SignalOfSTORSI signalOfSTORSI = new SignalOfSTORSI(ArrayTools.subArray(resultsSTORSI.arrayOfPercentK, 0, periodWindow), ArrayTools.subArray(resultsSTORSI.arrayOfPercentD, 0, periodWindow), SignalControl.periodAverageForSTORSI);
			SignalOfRSI signalOfRSI = new SignalOfRSI(ArrayTools.subArray(resultsRSI.arrayOfRSI, 0, periodWindow), SignalControl.periodAverageForRSI);
			SignalOfTRIX signalOfTRIX = new SignalOfTRIX(ArrayTools.subArray(resultsTRIX.arrayOfTRIX, 0, periodWindow), SignalControl.periodAverageForTRIX);
			
			signal.reset();
			//signal.addSignalMetrics(signalOfPPC.getSignal(), signalOfCCI.getSignal(), signalOfMACD.getSignal());
			signal.addSignalMetrics(signalOfMACD.getSignal());
			
			chart.listOfDate.add(typeQuoteSlice.dateTime);
			chart.listOfPrice.add(typeQuoteSlice.priceClose);
			chart.listOfSignalADX.add(signalOfADX.getSignal().strength);
			chart.listOfSignalCCI.add(signalOfCCI.getSignal().strength);
			chart.listOfSignalPPC.add(signalOfPPC.getSignal().strength);
			chart.listOfSignalMACD.add(signalOfMACD.getSignal().strength);
			chart.listOfSignalSTORSI.add(signalOfSTORSI.getSignal().strength);
			chart.listOfSignalRSI.add(signalOfRSI.getSignal().strength);
			chart.listOfSignalTRIX.add(signalOfTRIX.getSignal().strength);
			
			chart.listOfADX.add(analysisOfADXResult);
			chart.listOfCCI.add(analysisOfCCIResult);
			chart.listOfMACD.add(analysisOfMACDResult);
			chart.listOfRSI.add(analysisOfRSIResult);
			
			if (algorithmListener != null){
				algorithmListener.recieveSignal(signal, typeQuoteSlice);
			}
			
			ArrayList<String> columnValues = new ArrayList<String>();
			
//			columnValues.add(DateTools.getPrettyDate(typeQuoteSlice.dateTime));
//			columnValues.add(String.valueOf(typeQuoteSlice.priceClose));
//			columnValues.add(String.valueOf(StringTools.addPlusToPositiveNumbers(MathTools.roundToTwoDecimalPlaces(typeQuoteSlice.priceClose - listOfQuoteSlice.get(listOfQuoteSlice.size()-2).priceClose))));
//			columnValues.add(String.valueOf(signalOfMACD.getValue()));
//			columnValues.add(String.valueOf(signalOfMACD.getSignal().strength + "," + signalOfMACD.getSignal().signalTypeMetric.name()));
//			columnValues.add(String.valueOf(signalOfPPC.getValue()));
//			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfADXResult)));
//			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfCCIResult)));
//			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfBBResultUpper)));
//			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfBBResultLower)));
//			columnValues.add(String.valueOf(analysisOfMACDResult));
//			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfSTORSIResultK)));
//			columnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(analysisOfSTORSIResultD)));
			
			columnValues.add(DateTools.getPrettyDate(typeQuoteSlice.dateTime));
			columnValues.add(String.valueOf(typeQuoteSlice.priceClose));
			columnValues.add(String.valueOf(StringTools.addPlusToPositiveNumbers(MathTools.roundToTwoDecimalPlaces(typeQuoteSlice.priceClose - listOfQuoteSlice.get(listOfQuoteSlice.size()-2).priceClose))));
			columnValues.add(String.valueOf(signalOfPPC.getSignal().strength));
			columnValues.add(String.valueOf(signalOfADX.getSignal().strength));
			columnValues.add(String.valueOf(signalOfCCI.getSignal().strength));
			columnValues.add(String.valueOf(signalOfMACD.getSignal().strength));
			columnValues.add(String.valueOf(signalOfSTORSI.getSignal().strength));
			columnValues.add(String.valueOf(signalOfTRIX.getSignal().strength));
			columnValues.add(String.valueOf(signal.getCombinedSignal()));
			
			PositionGovernorResponse positionGovenorResponse = positionGovener.informGovener(typeQuoteSlice, signal);
			
			if (positionGovenorResponse.changedPosition){
				columnValues.add(String.valueOf(signal.currentSignalType.name()));
			}else{
				columnValues.add("");
			}
			
			listOfDisplayRows.add(columnValues);	
		}
	}

	@Override
	public void endOfFeed() {
		Co.println("Received end of feed...");
		bench.total();
		if (algorithmListener != null){
			algorithmListener.endOfAlgorithm();
		}
		PositionManager.instance.induceSellAll();
		Co.println("Account balance: " + Account.instance.getBankBalance() + " Fees paid: " + Account.instance.getTransactionFeesPaid());
		//chart.display();
		//new TableController().displayTable(AsciiTables.analysis_test, listOfDisplayRows);
		new TableController().displayTable(AsciiTables.algorithm_test, listOfDisplayRows);
	}
}
