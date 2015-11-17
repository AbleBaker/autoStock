/**
 * 
 */
package com.autoStock.backtest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.temporal.TemporalDataDescription;
import org.encog.ml.data.temporal.TemporalDataDescription.Type;
import org.encog.ml.data.temporal.TemporalMLDataSet;
import org.encog.ml.data.temporal.TemporalPoint;
import org.encog.ml.train.MLTrain;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.pso.NeuralPSO;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;
import org.encog.util.time.TimeUnit;

import com.autoStock.Co;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.indicator.CommonAnalysisData;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.QuoteSliceTools;
import com.autoStock.tools.StringTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
import com.autoStock.misc.Pair;

/**
 * @author Kevin
 *
 */
public class BacktestPredictFuture {
	private static final int INPUT_POINTS = 10;
	private static final int OUTPUT_POINTS = 1;
	private static final int INPUT_NEURONS = INPUT_POINTS * 4;
	private static final int OUTPUT_NEURONS = OUTPUT_POINTS * 1;
	private static final int IDEAL_OFFSET = 8;
	
	private Exchange exchange = new Exchange("NYSE");
	private Symbol symbol = new Symbol("MS", SecurityType.type_stock);
	private Date dateStart = DateTools.getDateFromString("09/02/2014");
	private Date dateEnd = DateTools.getDateFromString("09/30/2014");
	private double crossValidationRatio = 0d; 
	private ArrayList<PredictionResult> listOfPredictionResult = new ArrayList<PredictionResult>();
	private static final int PERIOD_LENGTH = 2;
	private PredictionResult result = new PredictionResult(0, 0);
	
	private static class PredictionResult {
		public int countCorrect = 0;
		public int countIncorrect = 0;
		
		public PredictionResult(int countCorrect, int countIncorrect) {
			this.countCorrect = countCorrect;
			this.countIncorrect = countIncorrect;
		}
	}
	
	public void run(){
		// Load the data
		HistoricalData historicalDataIS = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), DateTools.getDateFromString("01/03/2014"), DateTools.getDateFromString("01/03/2015"), Resolution.min);
		historicalDataIS.setStartAndEndDatesToExchange();
		ArrayList<DbStockHistoricalPrice> listOfResultsIS = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalDataIS.symbol.symbolName), new QueryArg(QueryArgs.exchange, new Exchange("NYSE").exchangeName), new QueryArg(QueryArgs.resolution, Resolution.min.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalDataIS.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalDataIS.endDate)));
		
//		HistoricalData historicalDataOS = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), DateTools.getDateFromString("04/02/2014"), DateTools.getDateFromString("05/01/2014"), Resolution.min);
//		historicalDataOS.setStartAndEndDatesToExchange();
//		ArrayList<DbStockHistoricalPrice> listOfResultsOS = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalDataOS.symbol.symbolName), new QueryArg(QueryArgs.exchange, new Exchange("NYSE").exchangeName), new QueryArg(QueryArgs.resolution, Resolution.min.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalDataOS.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalDataOS.endDate)));
//
//		Co.println("--> ");
		
		BasicMLDataSet dataSet = new BasicMLDataSet();
		
		// Build the input & ideal lists as % deltas
		ArrayList<Pair<DbStockHistoricalPrice, MLData>> priceIdealList = new ArrayList<>();
		
		CommonAnalysisData commonAnalysisData = new CommonAnalysisData();
		commonAnalysisData.setAnalysisData(QuoteSliceTools.getListOfQuoteSliceFromDbStockHistoricalPrice(listOfResultsIS));
		
		double pero[] = new double[commonAnalysisData.length()];
		double perh[] = new double[commonAnalysisData.length()];
		double perl[] = new double[commonAnalysisData.length()];
		double perc[] = new double[commonAnalysisData.length()];
		double peroc[] = new double[commonAnalysisData.length()];
		double perlh[] = new double[commonAnalysisData.length()];
		double ideal[] = new double[commonAnalysisData.length()];
		
		int c = 0;
		
		int IDEAL_OFFSET = 1;
		
		for (int i=1; i<commonAnalysisData.length() - IDEAL_OFFSET; i++){
			pero[i] = (commonAnalysisData.arrayOfPriceOpen[i-1] / commonAnalysisData.arrayOfPriceOpen[i]) -1;
			perh[i] = (commonAnalysisData.arrayOfPriceHigh[i-1] / commonAnalysisData.arrayOfPriceHigh[i]) -1;
			perl[i] = (commonAnalysisData.arrayOfPriceLow[i-1] / commonAnalysisData.arrayOfPriceLow[i]) -1;
			perc[i] = (commonAnalysisData.arrayOfPriceClose[i-1] / commonAnalysisData.arrayOfPriceClose[i]) -1;
			peroc[i] = (commonAnalysisData.arrayOfPriceOpen[i] / commonAnalysisData.arrayOfPriceClose[i]) -1;
			perlh[i] = (commonAnalysisData.arrayOfPriceLow[i] / commonAnalysisData.arrayOfPriceHigh[i]) -1;
			
//			pero[i] = (commonAnalysisData.arrayOfPriceOpen[i-2] / commonAnalysisData.arrayOfPriceOpen[i-1]) -1;
//			perh[i] = (commonAnalysisData.arrayOfPriceHigh[i-2] / commonAnalysisData.arrayOfPriceHigh[i-1]) -1;
//			perl[i] = (commonAnalysisData.arrayOfPriceLow[i-2] / commonAnalysisData.arrayOfPriceLow[i-1]) -1;
//			perc[i] = (commonAnalysisData.arrayOfPriceClose[i-2] / commonAnalysisData.arrayOfPriceClose[i-1]) -1;
//			
//			pero[i] = (commonAnalysisData.arrayOfPriceOpen[i-3] / commonAnalysisData.arrayOfPriceOpen[i-2]) -1;
//			perh[i] = (commonAnalysisData.arrayOfPriceHigh[i-3] / commonAnalysisData.arrayOfPriceHigh[i-2]) -1;
//			perl[i] = (commonAnalysisData.arrayOfPriceLow[i-3] / commonAnalysisData.arrayOfPriceLow[i-2]) -1;
//			perc[i] = (commonAnalysisData.arrayOfPriceClose[i-3] / commonAnalysisData.arrayOfPriceClose[i-2]) -1;
			
			ideal[i] = (commonAnalysisData.arrayOfPriceLow[i] / commonAnalysisData.arrayOfPriceHigh[i+IDEAL_OFFSET]) -1;
			
			BasicMLDataPair pair = new BasicMLDataPair(new BasicMLData(new double[]{pero[i], perh[i], perl[i], perc[i], peroc[i], perlh[i]}), new BasicMLData(new double[]{ideal[i]}));
			dataSet.add(pair);
		}
		
		BasicNetwork network = getMLNetwork(6, 1);
		network.reset();
		
		MLTrain train = new ResilientPropagation(network, dataSet);
		
		for (int i=0; i<1000; i++){
			train.iteration();
			Co.println(i + ". " + train.getError());
		}
		
		for (int i=0; i<dataSet.getRecordCount(); i++){
			double idealValue = dataSet.getData().get(i).getIdeal().getData(0);
			double computedValue = network.compute(dataSet.getData().get(i).getInput()).getData(0);
			
			//Co.println("--> Inputs, ideal, computed: " + StringTools.arrayOfDoubleToString(dataSet.getData().get(i).getInputArray()) + "... " + idealValue + " = " + computedValue);
			
			
			if (idealValue > 0 && computedValue > 0){result.countCorrect++;}
			else if (idealValue < 0 && computedValue < 0){result.countCorrect++;}
			//else if (idealValue == 0){}
			else {result.countIncorrect++;}
		}
		
		Co.println("--> Directional accuracy: " + result.countIncorrect + " / " + result.countCorrect + " = " + new DecimalFormat("#.##").format(((double)result.countCorrect / ((double)result.countCorrect + (double)result.countIncorrect) * 100)) + "%");
	}
	
	public BasicNetwork getMLNetwork(int inputSize, int outputSize){
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(inputSize);
		pattern.addHiddenLayer(inputSize/2);
		//pattern.addHiddenLayer(inputSize/3);
		pattern.setOutputNeurons(outputSize);
		pattern.setActivationFunction(new ActivationTANH());
		pattern.setActivationOutput(new ActivationTANH());
		return (BasicNetwork) pattern.generate();
	}
}
