/**
 * 
 */
package com.autoStock.backtest;

import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import javax.swing.JPanel;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
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
import org.encog.neural.networks.training.propagation.resilient.RPROPType;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.pso.NeuralPSO;
import org.encog.neural.pattern.ElmanPattern;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.pattern.JordanPattern;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;
import org.encog.util.time.TimeUnit;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.axis.Timeline;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.slf4j.helpers.SubstituteLoggerFactory;

import com.autoStock.Co;
import com.autoStock.chart.ChartDataFiller;
import com.autoStock.chart.ChartForAlgorithmTest.TimeSeriesType;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.indicator.CommonAnalysisData;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.signal.extras.EncogNetworkProvider;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.QuoteSliceTools;
import com.autoStock.tools.ResultsTools;
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
	private static final int DAY_GAPS_SIZE = 5;
	private static final boolean DISCARD = true;
	private Exchange exchange = new Exchange("NYSE");
	private Symbol symbol = new Symbol("MS", SecurityType.type_stock);
	private Date dateStart = DateTools.getDateFromString("03/04/2014");
	private Date dateEnd = DateTools.getDateFromString("03/06/2014");
	private double crossValidationRatio = 0; //0.30;
	private ArrayList<PredictionResult> listOfPredictionResult = new ArrayList<PredictionResult>();
	private PredictionResult result = new PredictionResult(0, 0);
	
	private BasicMLDataSet mlDataSetReg;
	private BasicMLDataSet mlDataSetCross;
	private DefaultHighLowDataset dataSetForDefaultHighLowDataset;
	private TimeSeriesCollection TSC = new TimeSeriesCollection();
	private NormalizedField normalizer = new NormalizedField(NormalizationAction.Normalize, null, 0.010, -0.010, 1, -1); 
	
	public BacktestPredictFuture(){
		Global.callbackLock.requestLock();
	}
	
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
		HistoricalData historicalData = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), dateStart, dateEnd, Resolution.min);
		historicalData.setStartAndEndDatesToExchange();
		ArrayList<DbStockHistoricalPrice> listOfResultsIS = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.name), new QueryArg(QueryArgs.exchange, new Exchange("NYSE").name), new QueryArg(QueryArgs.resolution, Resolution.min.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));
		
		BacktestUtils.pruneToExchangeHours(listOfResultsIS, new Exchange("NYSE"));
		
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
		
		Co.println("--> C: " + commonAnalysisData.length());
		
		for (int i=0; i<15; i++){
			Co.println("--> PC: " + commonAnalysisData.arrayOfPriceClose[i]);
		}
		
		int INPUT_PER_ITEM = 15;
		int IDEAL_OFFSET = 5;
		int IDEAL_SIZE = 1 ;
		
		int discard = 0;
		Date lastDate = null;
		ArrayList<Integer> discards = new ArrayList<>();
		
		for (int i=0; i<commonAnalysisData.length() - IDEAL_OFFSET - INPUT_PER_ITEM - IDEAL_SIZE; i++){
			
			if (lastDate == null){lastDate = (Date) commonAnalysisData.arrayOfDates[i].clone(); lastDate.setHours(0); lastDate.setMinutes(0); lastDate.setSeconds(0);}
			Date predDate = (Date)commonAnalysisData.arrayOfDates[i + IDEAL_OFFSET + IDEAL_SIZE].clone(); predDate.setHours(0); predDate.setMinutes(0); predDate.setSeconds(0);

			if (predDate.getTime() != lastDate.getTime()){
				//Co.println("--> Prediction spans into " + lastDate + " to " + predDate);
				lastDate = predDate;
				discard = INPUT_PER_ITEM + IDEAL_OFFSET;
			}
			
			if (DISCARD && discard > 0 && i < commonAnalysisData.length() - (IDEAL_OFFSET + IDEAL_SIZE)){
				Co.println("--> Discarding: " + commonAnalysisData.arrayOfDates[i + 1 + IDEAL_OFFSET]);
				discards.add(i + 1 + IDEAL_OFFSET);
				discard--; continue;
			}
			
			pero[i] = (commonAnalysisData.arrayOfPriceOpen[i + 1] / commonAnalysisData.arrayOfPriceOpen[i]) -1;
			perh[i] = (commonAnalysisData.arrayOfPriceHigh[i + 1] / commonAnalysisData.arrayOfPriceHigh[i]) -1;
			perl[i] = (commonAnalysisData.arrayOfPriceLow[i + 1] / commonAnalysisData.arrayOfPriceLow[i]) -1;
			perc[i] = (commonAnalysisData.arrayOfPriceClose[i + 1] / commonAnalysisData.arrayOfPriceClose[i]) -1;
			
			//peroc[i] = (commonAnalysisData.arrayOfPriceOpen[i + 1] / commonAnalysisData.arrayOfPriceClose[i + 1]) -1;
			//perlh[i] = (commonAnalysisData.arrayOfPriceLow[i + 1] / commonAnalysisData.arrayOfPriceHigh[i + 1]) -1;
			
			Co.println("--> Diff: " + commonAnalysisData.arrayOfPriceClose[i] + " -> " + commonAnalysisData.arrayOfPriceClose[i + 1] + " = " + new DecimalFormat("#.#######").format(perc[i]));
			
			if (i >= INPUT_PER_ITEM){
				Co.println("--> Last input window date: " + commonAnalysisData.arrayOfDates[i + 1]);
				
				double[] ideal = new double[IDEAL_SIZE];
						
				for (int idealIndex = 0; idealIndex < IDEAL_SIZE; idealIndex++){
					int idealInSet = i + 1 + IDEAL_OFFSET + idealIndex;
					ideal[idealIndex] = normalizer.normalize((commonAnalysisData.arrayOfPriceClose[i + 1 + IDEAL_OFFSET + idealIndex] / commonAnalysisData.arrayOfPriceClose[i + IDEAL_OFFSET]) -1);
					Co.println("--> Added ideal: " + commonAnalysisData.arrayOfDates[idealInSet] + ", " + new DecimalFormat("#.#######").format(ideal[idealIndex]) + " from price " + commonAnalysisData.arrayOfPriceClose[i + IDEAL_OFFSET] + " to price " + (commonAnalysisData.arrayOfPriceClose[i + 1 + IDEAL_OFFSET + idealIndex]));
					
				}
				
				ArrayList<Double> input = new ArrayList<Double>();
				input.addAll(ListTools.getListFromArray(Arrays.copyOfRange(pero, i -INPUT_PER_ITEM, i + 1)));
				input.addAll(ListTools.getListFromArray(Arrays.copyOfRange(perh, i -INPUT_PER_ITEM, i + 1)));
				input.addAll(ListTools.getListFromArray(Arrays.copyOfRange(perl, i -INPUT_PER_ITEM, i + 1)));
				input.addAll(ListTools.getListFromArray(Arrays.copyOfRange(perc, i -INPUT_PER_ITEM, i + 1)));
				
				//input.addAll(ListTools.getListFromArray(Arrays.copyOfRange(peroc, i -INPUT_PER_ITEM, i + 1)));
				//input.addAll(ListTools.getListFromArray(Arrays.copyOfRange(perlh, i -INPUT_PER_ITEM, i + 1)));
				
				for (int r=0; r<INPUT_PER_ITEM; r++){
//					input[r] = normalizer.normalize(input[r]);
//					input.add(normalizer.normalize(Arrays.copyOfRange(perc, i -INPUT_PER_ITEM, i + 1)[r]));
					
					input.set(r, normalizer.normalize(input.get(r)));
				}
				
				BasicMLDataPair pair = new BasicMLDataPair(
				new BasicMLData(ArrayTools.getArrayFromListOfDouble(input)),
//				new BasicMLData(input),
				new BasicMLData(ideal));
				dataSet.add(pair);
				
				Co.println("\n");
			}
		}
		
		int removed = 0;
		
		for (Integer i : discards){
			Co.println("--> NEED TO DISCARD at: " + i);
			commonAnalysisData.remove(i - removed);
			removed++;
		}
		
		double max = 0;
		
		for (MLDataPair pair : BasicMLDataSet.toList(dataSet)){
			max = Math.max(pair.getIdeal().getData(0), max);
		}
		
		int crossCount = 0;
		int regularEnd = 0; 
		
		if (crossValidationRatio == 0){
			mlDataSetReg = dataSet;
		} else{
			crossCount = (int) (crossValidationRatio * (double)dataSet.size());
			regularEnd = dataSet.size() - crossCount;
			
			Co.println("--> DSSize: " + dataSet.size());
			Co.println("--> Regular end: " + regularEnd);
			Co.println("--> Cross start: " + (regularEnd + 1));
			
			//mlDataSetReg = dataSet;
			mlDataSetReg = new BasicMLDataSet(BasicMLDataSet.toList(dataSet).subList(0, regularEnd));
			mlDataSetCross = new BasicMLDataSet(BasicMLDataSet.toList(dataSet).subList(regularEnd, dataSet.size()));
			
			Co.println("--> Check sizes: " + mlDataSetReg.size() + ", " + mlDataSetCross.size());
		}
		
		BasicNetwork network = getMLNetwork(dataSet.getInputSize(), 1);
		network.reset();
		
		MLTrain train = new ResilientPropagation(network, mlDataSetReg);
		((ResilientPropagation)train).setRPROPType(RPROPType.iRPROPp);
		
		for (int i=0; i<512; i++){
			train.iteration();
			Co.println(i + ". " + train.getError());
		}
		
		train.finishTraining();
		
		//mlDataSetReg = new BasicMLDataSet(BasicMLDataSet.toList(dataSet).subList(0, regularEnd));
		
		new EncogNetworkProvider().saveNetwork((BasicNetwork) train.getMethod(), historicalData.exchange.name + "-" + historicalData.symbol.name + "-predict");
		
		// Compute Regular
		for (int i=0; i<mlDataSetReg.getRecordCount(); i++){
			double idealValue = normalizer.deNormalize(mlDataSetReg.getData().get(i).getIdeal().getData(0));
			double computedValue = normalizer.deNormalize(network.compute(mlDataSetReg.getData().get(i).getInput()).getData(0));
			
			double[] input = dataSet.getData().get(i).getInputArray();
			double[] inputDenorm = new double[input.length];
			
			for (int r=0; r<input.length; r++){
				inputDenorm[r] = normalizer.deNormalize(input[r]);
			}
			
			//Co.println("--> Inputs, ideal, computed: " + StringTools.arrayOfDoubleToString(inputDenorm) + " = " + new DecimalFormat("#.######").format(idealValue) + " / " + new DecimalFormat("#.######").format(computedValue));
			
			if (idealValue > 0 && computedValue > 0){result.countCorrect++;}
			else if (idealValue < 0 && computedValue < 0){result.countCorrect++;}
			else if (idealValue == 0){} // && MathTools.roundTo(computedValue, 4) == 0){result.countCorrect++;}
			else {result.countIncorrect++;}
		}
		
		Co.println("--> Directional accuracy (reg): " + result.countIncorrect + " / " + result.countCorrect + " = " + new DecimalFormat("#.##").format(((double)result.countCorrect / ((double)result.countCorrect + (double)result.countIncorrect) * 100)) + "%");
		
		
		//TSC.addSeries(new ChartDataFiller().getTimeSeries("Price ($)", ResultsTools.getBasicPair(commonAnalysisData.arrayOfDates, commonAnalysisData.arrayOfPriceClose)));
		
		TimeSeries tsPrice = new TimeSeries("Price");
		
		Date day = null;
		Date cur = null;
		
		for (int i=0; i<commonAnalysisData.length(); i++){
			if (day == null){day = (Date) commonAnalysisData.arrayOfDates[i].clone();} day.setHours(0); day.setMinutes(0); day.setSeconds(0);
			cur = (Date) commonAnalysisData.arrayOfDates[i].clone(); cur.setHours(0); cur.setMinutes(0); cur.setSeconds(0);
			
			if (day.getTime() != cur.getTime()){
				Date temp = (Date) commonAnalysisData.arrayOfDates[i -1].clone();
				
				for (int x=1; x<=DAY_GAPS_SIZE; x++){
					temp = DateTools.getRolledDate(temp, Calendar.MINUTE, 1);
					tsPrice.add(new TimeSeriesDataItem(new Minute(temp), null));
					Co.println("--> Added null at: " + temp);
				}
				
				day = cur;
			}
			
			tsPrice.add(new TimeSeriesDataItem(new Minute(commonAnalysisData.arrayOfDates[i]), commonAnalysisData.arrayOfPriceClose[i]));
		}
		
		TSC.addSeries(tsPrice);
		
		TimeSeries tsReg = new TimeSeries("Regular");
		
		day = null;
		cur = null;
		
		for (int i=0; i<mlDataSetReg.getRecordCount(); i++){
			//Co.println("--> Computed: " + network.compute(mlDataSetReg.get(i + offset).getInput()).getData(0));
			
			double computed = normalizer.deNormalize(network.compute(mlDataSetReg.get(i).getInput()).getData(0));
			double currentPriceClose = commonAnalysisData.arrayOfPriceClose[i + INPUT_PER_ITEM + IDEAL_OFFSET];
			Date currentDate = commonAnalysisData.arrayOfDates[i + INPUT_PER_ITEM + IDEAL_OFFSET + 1];
			double value =  currentPriceClose * ( 1 + computed); 
			
//			value = MathTools.round(value); 
//			Co.println("--> Current price: " + currentPriceClose);
//			Co.println("--> Computed: " + new DecimalFormat("#.######").format(computed));
//			Co.println("--> Actual: " + new DecimalFormat("#.######").format(normalizer.deNormalize(mlDataSetReg.get(i).getIdeal().getData(0))));
//			Co.println("--> Result: " + value);
//			Co.println("\n");
			
			if (day == null){day = (Date) commonAnalysisData.arrayOfDates[i + INPUT_PER_ITEM + IDEAL_OFFSET].clone(); day.setHours(0); day.setMinutes(0); day.setSeconds(0);}
			Date curDate = (Date)commonAnalysisData.arrayOfDates[i + INPUT_PER_ITEM + IDEAL_OFFSET].clone(); curDate.setHours(0); curDate.setMinutes(0); curDate.setSeconds(0);
			
			if (day.getTime() != curDate.getTime()){
				day = curDate;
				Co.println("--> DAY CHANGE to: " + curDate);

				Date temp = (Date)commonAnalysisData.arrayOfDates[i + INPUT_PER_ITEM + IDEAL_OFFSET -1].clone();
				
				for (int x=1; x<=DAY_GAPS_SIZE; x++){
					temp = DateTools.getRolledDate(temp, Calendar.MINUTE, 1);
					tsReg.add(new TimeSeriesDataItem(new Minute(temp), null));
					Co.println("--> Added null at (B): " + temp);
				}
			}
			
			tsReg.add(new TimeSeriesDataItem(new Minute(currentDate), value)); 
		}
		
		TSC.addSeries(tsReg);
		
		// Compute Cross
		
		if (crossValidationRatio > 0){
			result.countCorrect = 0;
			result.countIncorrect = 0;
			
			for (int i=0; i<mlDataSetCross.getRecordCount(); i++){
				double idealValue = normalizer.deNormalize(mlDataSetCross.getData().get(i).getIdeal().getData(0));
				double computedValue = normalizer.deNormalize(network.compute(mlDataSetCross.getData().get(i).getInput()).getData(0));
				
				double[] input = dataSet.getData().get(i).getInputArray();
				double[] inputDenorm = new double[input.length];
				
				for (int r=0; r<input.length; r++){
					inputDenorm[r] = normalizer.deNormalize(input[r]);
				}
				
				//Co.println("--> Inputs, ideal, computed: " + StringTools.arrayOfDoubleToString(inputDenorm) + " = " + new DecimalFormat("#.######").format(idealValue) + " / " + new DecimalFormat("#.######").format(computedValue));
				
				if (idealValue > 0 && computedValue > 0){result.countCorrect++;}
				else if (idealValue < 0 && computedValue < 0){result.countCorrect++;}
				else if (idealValue == 0 && MathTools.roundTo(computedValue, 4) == 0){result.countCorrect++;}
				else {result.countIncorrect++;}
			}
			
			Co.println("--> Directional accuracy (cross): " + result.countIncorrect + " / " + result.countCorrect + " = " + new DecimalFormat("#.##").format(((double)result.countCorrect / ((double)result.countCorrect + (double)result.countIncorrect) * 100)) + "%");
			
			TimeSeries tsCross = new TimeSeries("Cross");
			
			for (int i=0; i<mlDataSetCross.getRecordCount(); i++){
				//Co.println("--> Computed: " + network.compute(mlDataSetReg.get(i + offset).getInput()).getData(0));
				
				double computed = normalizer.deNormalize(network.compute(mlDataSetCross.get(i).getInput()).getData(0));
				double currentPriceClose = commonAnalysisData.arrayOfPriceClose[regularEnd + i + INPUT_PER_ITEM + IDEAL_OFFSET];
				Date currentDate = commonAnalysisData.arrayOfDates[regularEnd + i + INPUT_PER_ITEM + IDEAL_OFFSET + 1];
				double value =  currentPriceClose * ( 1 + computed); 
				
//				value = MathTools.round(value); 
//				Co.println("--> Current price: " + currentPriceClose);
//				Co.println("--> Computed: " + new DecimalFormat("#.######").format(computed));
//				Co.println("--> Actual: " + new DecimalFormat("#.######").format(normalizer.deNormalize(mlDataSetReg.get(i).getIdeal().getData(0))));
//				Co.println("--> Result: " + value);
//				Co.println("\n");
				
				tsCross.add(new TimeSeriesDataItem(new Minute(currentDate), value)); 
			}
			
			TSC.addSeries(tsCross);
		}
		
		dataSetForDefaultHighLowDataset = new DefaultHighLowDataset("Price series", commonAnalysisData.arrayOfDates, commonAnalysisData.arrayOfPriceHigh, commonAnalysisData.arrayOfPriceLow, commonAnalysisData.arrayOfPriceOpen, commonAnalysisData.arrayOfPriceClose, ArrayTools.convertToDouble(commonAnalysisData.arrayOfSizeVolume));
		
		new PredictDisplay("autoStock - Forward Price Prediction");
	}
	
	public BasicNetwork getMLNetwork(int inputSize, int outputSize){
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(inputSize);
		pattern.addHiddenLayer((int)((double)inputSize/1.5));
		pattern.addHiddenLayer(inputSize/2);
		pattern.addHiddenLayer(inputSize/3);
		//pattern.addHiddenLayer(inputSize/5);
		pattern.setOutputNeurons(outputSize);
		pattern.setActivationFunction(new ActivationTANH());
		pattern.setActivationOutput(new ActivationTANH());
		return (BasicNetwork) pattern.generate();
	}
	
	
	public class PredictDisplay extends ApplicationFrame {
		public PredictDisplay(String title) {
			super(title);
			
			final ChartPanel chartPanel = (ChartPanel) createPanel();
			chartPanel.setPreferredSize(new Dimension(1600, 1000));
			chartPanel.setHorizontalAxisTrace(true);
			setContentPane(chartPanel);
			setVisible(true);
			toFront();
			pack();
			RefineryUtilities.positionFrameOnScreen(this, 0, 0);
		}
		
		public JPanel createPanel() {
			JFreeChart chart = createChart();
			chart.setAntiAlias(true);
			chart.setTextAntiAlias(true);
			ChartPanel panel = new ChartPanel(chart, false);
			panel.setFillZoomRectangle(true);
			panel.setMouseWheelEnabled(true);
			return panel;
		}
		
		private JFreeChart createChart() {
			CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new DateAxis("Domain"));
			
			SegmentedTimeline stl = new SegmentedTimeline(SegmentedTimeline.MINUTE_SEGMENT_SIZE, 390 + DAY_GAPS_SIZE, 1050 - DAY_GAPS_SIZE);
			stl.setStartTime(BacktestPredictFuture.this.dateStart.getTime());
			((DateAxis)plot.getDomainAxis()).setTimeline(stl);

			plot.setGap(10);
			plot.setOrientation(PlotOrientation.VERTICAL);
			plot.setBackgroundPaint(Color.lightGray);
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);
			plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
			
			try {
				XYPlot subPlotForCandleStick = ChartFactory.createCandlestickChart("Candlestick Demo", "Time", "Candle Stick", dataSetForDefaultHighLowDataset, false).getXYPlot();
				((NumberAxis) subPlotForCandleStick.getRangeAxis()).setAutoRangeIncludesZero(false);
				subPlotForCandleStick.setBackgroundPaint(Color.white);
				subPlotForCandleStick.setDomainGridlinePaint(Color.lightGray);
				subPlotForCandleStick.setRangeGridlinePaint(Color.lightGray);
				subPlotForCandleStick.setRangeAxis(0, new NumberAxis("Price"));
				subPlotForCandleStick.getRangeAxis(0).setAutoRange(true);
				((NumberAxis) subPlotForCandleStick.getRangeAxis(0)).setAutoRangeIncludesZero(false);
				subPlotForCandleStick.setRangeAxis(1, new NumberAxis("Volume"));
				
				((CandlestickRenderer) subPlotForCandleStick.getRenderer()).setUseOutlinePaint(true);
				plot.add(subPlotForCandleStick, 1);
				
				XYPlot subPlotForPrice = new XYPlot(TSC, null, new NumberAxis("Action"), new StandardXYItemRenderer());
				subPlotForPrice.getRenderer().setSeriesPaint(0, Color.DARK_GRAY);
				subPlotForPrice.getRangeAxis().setAutoRange(true);
				((NumberAxis) subPlotForPrice.getRangeAxis()).setAutoRangeIncludesZero(false);
				subPlotForPrice.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
				subPlotForPrice.getRenderer().setSeriesPaint(1, Color.RED);
							
				plot.add(subPlotForPrice);
			} catch (Exception e) {}

			DateAxis axis = (DateAxis) plot.getDomainAxis();
			axis.setDateFormatOverride(new SimpleDateFormat("MM/dd HH:mm"));

			JFreeChart chart = new JFreeChart("autoStock - Fordward Price Prediction", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
			chart.setBackgroundPaint(Color.white);
			return chart;
		}
	}
}
