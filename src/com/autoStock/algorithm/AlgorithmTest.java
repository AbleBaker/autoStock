package com.autoStock.algorithm;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.analysis.AnalysisOfBB;
import com.autoStock.analysis.AnalysisOfCCI;
import com.autoStock.analysis.AnalysisOfDI;
import com.autoStock.analysis.AnalysisOfMACD;
import com.autoStock.analysis.AnalysisOfRSI;
import com.autoStock.analysis.AnalysisOfTRIX;
import com.autoStock.analysis.CommonAnlaysisData;
import com.autoStock.analysis.results.ResultsBB;
import com.autoStock.analysis.results.ResultsCCI;
import com.autoStock.analysis.results.ResultsDI;
import com.autoStock.analysis.results.ResultsMACD;
import com.autoStock.analysis.results.ResultsRSI;
import com.autoStock.analysis.results.ResultsTRIX;
import com.autoStock.chart.ChartForAlgorithmTest;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionGovernor;
import com.autoStock.position.PositionGovernorResponse;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseStatus;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalControl;
import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalOfCCI;
import com.autoStock.signal.SignalOfDI;
import com.autoStock.signal.SignalOfMACD;
import com.autoStock.signal.SignalOfPPC;
import com.autoStock.signal.SignalOfRSI;
import com.autoStock.signal.SignalOfTRIX;
import com.autoStock.signal.SignalTools;
import com.autoStock.taLib.MAType;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.StringTools;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class AlgorithmTest extends AlgorithmBase implements ReceiverOfQuoteSlice {
	private int periodLength = SignalControl.periodLength;
	private PositionGovernor positionGovener = PositionGovernor.instance;
	private CommonAnlaysisData commonAnlaysisData = new CommonAnlaysisData();
	private AnalysisOfCCI analysisOfCCI = new AnalysisOfCCI(periodLength, false, commonAnlaysisData);
	private AnalysisOfDI analysisOfDI = new AnalysisOfDI(periodLength, false, commonAnlaysisData);
	private AnalysisOfMACD analysisOfMACD = new AnalysisOfMACD(periodLength, false, commonAnlaysisData);
	private AnalysisOfBB analysisOfBB = new AnalysisOfBB(periodLength, false, commonAnlaysisData);
	private AnalysisOfTRIX analysisOfTRIX = new AnalysisOfTRIX(periodLength, false, commonAnlaysisData);
	private AnalysisOfRSI analysisOfRSI = new AnalysisOfRSI(periodLength, false, commonAnlaysisData);

	private ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
	public ArrayList<QuoteSlice> listOfQuoteSlice = new ArrayList<QuoteSlice>();
	public Signal signal = new Signal(SignalSource.from_analysis);
	private ChartForAlgorithmTest chart;
	public QuoteSlice firstQuoteSlice;
	public QuoteSlice currentQuoteSlice;

	public AlgorithmTest(boolean canTrade, Exchange exchange, Symbol symbol, AlgorithmMode algorithmMode) {
		super(canTrade, exchange, symbol, algorithmMode);
		if (algorithmMode.displayChart) {
			chart = new ChartForAlgorithmTest();
		}
	}

	@Override
	public synchronized void receiveQuoteSlice(QuoteSlice quoteSlice) {
		if (algorithmMode.displayMessages) {
			Co.println("Received quote: " + quoteSlice.symbol + ", " + DateTools.getPrettyDate(quoteSlice.dateTime) + ", " + "O,H,L,C: " + +MathTools.round(quoteSlice.priceOpen) + ", " + MathTools.round(quoteSlice.priceHigh) + ", " + MathTools.round(quoteSlice.priceLow) + ", " + MathTools.round(quoteSlice.priceClose));
		}

		if (firstQuoteSlice == null) {
			firstQuoteSlice = quoteSlice;
		}
		currentQuoteSlice = quoteSlice;
		listOfQuoteSlice.add(quoteSlice);

		if (listOfQuoteSlice.size() > periodLength) {
			if (listOfQuoteSlice.size() > periodLength) {
				listOfQuoteSlice.remove(0);
			}

			commonAnlaysisData.setAnalysisData(listOfQuoteSlice);

			analysisOfCCI.setDataSet(listOfQuoteSlice);
			analysisOfDI.setDataSet(listOfQuoteSlice);
			analysisOfBB.setDataSet(listOfQuoteSlice);
			analysisOfMACD.setDataSet(listOfQuoteSlice);
			analysisOfRSI.setDataSet(listOfQuoteSlice);
			analysisOfTRIX.setDataSet(listOfQuoteSlice);

			ResultsCCI resultsCCI = analysisOfCCI.analyize();
			ResultsDI resultsDI = analysisOfDI.analize();
			ResultsBB resultsBB = analysisOfBB.analyize(MAType.Ema);
			ResultsMACD resultsMACD = analysisOfMACD.analize();
			ResultsRSI resultsRSI = analysisOfRSI.analyize();
			ResultsTRIX resultsTRIX = analysisOfTRIX.analyize();

			double[] arrayOfPriceClose = commonAnlaysisData.arrayOfPriceClose;
			double analysisOfCCIResult = resultsCCI.arrayOfCCI[0];
			double analysisOfDIResultPlus = resultsDI.arrayOfDIPlus[0];
			double analysisOfDIResultMinus = resultsDI.arrayOfDIMinus[0];
			double analysisOfBBResultUpper = resultsBB.arrayOfUpperBand[0];
			double analysisOfBBResultLower = resultsBB.arrayOfLowerBand[0];
			double analysisOfMACDResult = resultsMACD.arrayOfMACDSignal[0];
			double analysisOfRSIResult = resultsRSI.arrayOfRSI[0];
			double analysisOfTrixResult = resultsTRIX.arrayOfTRIX[0];

			SignalOfPPC signalOfPPC = new SignalOfPPC(ArrayTools.subArray(arrayOfPriceClose, 0, periodLength), SignalControl.periodAverageForPPC);
			SignalOfDI signalOfDI = new SignalOfDI(ArrayTools.subArray(resultsDI.arrayOfDIPlus, 0, 1), ArrayTools.subArray(resultsDI.arrayOfDIMinus, 0, 1), SignalControl.periodAverageForDI);
			SignalOfCCI signalOfCCI = new SignalOfCCI(ArrayTools.subArray(resultsCCI.arrayOfCCI, 0, 1), SignalControl.periodAverageForCCI);
			SignalOfMACD signalOfMACD = new SignalOfMACD(ArrayTools.subArray(resultsMACD.arrayOfMACDHistogram, 0, 1), SignalControl.periodAverageForMACD);
			SignalOfRSI signalOfRSI = new SignalOfRSI(ArrayTools.subArray(resultsRSI.arrayOfRSI, 0, 1), SignalControl.periodAverageForRSI);
			SignalOfTRIX signalOfTRIX = new SignalOfTRIX(ArrayTools.subArray(resultsTRIX.arrayOfTRIX, 0, 1), SignalControl.periodAverageForTRIX);

			signal.reset();
//			signal.addSignalMetrics(signalOfDI.getSignal());
			signal.addSignalMetrics(signalOfDI.getSignal(), signalOfRSI.getSignal(), signalOfTRIX.getSignal()); 

			if (algorithmMode.displayChart) {
				chart.listOfDate.add(quoteSlice.dateTime);
				chart.listOfPrice.add(quoteSlice.priceClose);
				chart.listOfSignalDI.add(signalOfDI.getSignal().strength);
				chart.listOfSignalCCI.add(signalOfCCI.getSignal().strength);
				chart.listOfSignalPPC.add(signalOfPPC.getSignal().strength);
				chart.listOfSignalMACD.add(signalOfMACD.getSignal().strength);
				chart.listOfSignalRSI.add(signalOfRSI.getSignal().strength);
				chart.listOfSignalTRIX.add(signalOfTRIX.getSignal().strength);
				chart.listOfSignalTotal.add((int) SignalTools.getCombinedSignal(signal).strength);

				chart.listOfDI.add(analysisOfDIResultPlus - analysisOfDIResultMinus);
				chart.listOfCCI.add(analysisOfCCIResult);
				chart.listOfMACD.add(analysisOfMACDResult);
				chart.listOfRSI.add(analysisOfRSIResult);
			}

			if (algorithmListener != null) {
				algorithmListener.recieveSignal(signal, quoteSlice);
			}

			ArrayList<String> columnValues = new ArrayList<String>();

			if (algorithmMode.displayTable) {
				columnValues.add(DateTools.getPrettyDate(quoteSlice.dateTime));
				columnValues.add(String.valueOf(MathTools.round(quoteSlice.priceClose)));
				columnValues.add(String.valueOf(StringTools.addPlusToPositiveNumbers(MathTools.round(quoteSlice.priceClose - listOfQuoteSlice.get(listOfQuoteSlice.size() - 2).priceClose))));
				columnValues.add(String.valueOf(signalOfPPC.getSignal().strength));
				columnValues.add(String.valueOf(signalOfDI.getSignal().strength));
				columnValues.add(String.valueOf(signalOfCCI.getSignal().strength));
				columnValues.add(String.valueOf(signalOfRSI.getSignal().strength));
				columnValues.add(String.valueOf(signalOfMACD.getSignal().strength));
				columnValues.add(String.valueOf(signalOfTRIX.getSignal().strength));
				columnValues.add(String.valueOf(SignalTools.getCombinedSignal(signal).strength));
			}

			PositionGovernorResponse positionGovenorResponse = positionGovener.informGovener(quoteSlice, signal, exchange, transactions, positionGovernorResponsePrevious);

			if (algorithmMode.displayTable) {
				if (positionGovenorResponse.status == PositionGovernorResponseStatus.failed){
					if (positionGovenorResponse.status != positionGovernorResponsePrevious.status && positionGovenorResponse.status.reason != positionGovernorResponsePrevious.status.reason){
						columnValues.add(signal.currentSignalPoint.name() + ", " + positionGovenorResponse.status.reason.name());
						columnValues.add("");
						columnValues.add("");
					}else{
						columnValues.add(""); columnValues.add(""); columnValues.add("");
					}
				} else if (positionGovenorResponse.status != PositionGovernorResponseStatus.no_change && positionGovenorResponse.status != PositionGovernorResponseStatus.none) {
					columnValues.add(signal.currentSignalPoint.name() + ", " + positionGovenorResponse.status.reason + ", " + positionGovenorResponse.position.positionType.name() + ", " + signal.currentSignalPoint.signalMetricType.name());
					columnValues.add(positionGovenorResponse.position.units + ", " + MathTools.round(positionGovenorResponse.position.lastKnownPrice) + ", " + MathTools.round(positionGovenorResponse.position.units * positionGovenorResponse.position.lastKnownPrice));
					columnValues.add(String.valueOf(Account.instance.getBankBalance()));
					
					handlePositionChange();
				} else {
					columnValues.add(""); columnValues.add(""); columnValues.add("");
				}
				
				listOfDisplayRows.add(columnValues);
			}
			
			positionGovernorResponsePrevious = positionGovenorResponse;
		}
	}

	@Override
	public synchronized void endOfFeed(Symbol symbol) {
		if (algorithmMode.displayChart) {
			chart.display();
		}
		if (algorithmMode.displayTable) {
			new TableController().displayTable(AsciiTables.algorithm_test, listOfDisplayRows);
		}
		if (algorithmListener != null) {
			algorithmListener.endOfAlgorithm();
		}
	}
}
