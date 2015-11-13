package com.autoStock.signal.signalMetrics;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.indicator.IndicatorOfEMA;
import com.autoStock.indicator.results.ResultsBase;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.IndicatorParameters;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCrossover extends SignalBase {
	public double ema1Value = 0;
	public double ema2Value = 0;
	
	public SignalOfCrossover(SignalMetricType signalMetricType, SignalParameters signalParameters, AlgorithmBase algorithmBase) {
		super(signalMetricType, signalParameters, algorithmBase);
	}
	
	@Override
	public void setInput(double value) {
		IndicatorOfEMA ema1 = new IndicatorOfEMA(new IndicatorParametersForEMAFirst(), commonAnalysisData, taLibCore, SignalMetricType.metric_crossover);
		IndicatorOfEMA ema2 = new IndicatorOfEMA(new IndicatorParametersForEMASecond(), commonAnalysisData, taLibCore, SignalMetricType.metric_crossover);
		
		ema1Value = ((ResultsBase)ema1.setDataSet().analyze()).getLast();
		ema2Value = ((ResultsBase)ema2.setDataSet().analyze()).getLast();
		
		super.setInput(ema1Value - ema2Value);
	}

//	@Override
//	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
//
//		return new SignalPoint();
//	}
	
	
	
	public static class IndicatorParametersForEMAFirst extends IndicatorParameters {
		public IndicatorParametersForEMAFirst() {super(new MutableInteger(10), 1);}
	}
	
	public static class IndicatorParametersForEMASecond extends IndicatorParameters {
		public IndicatorParametersForEMASecond() {super(new MutableInteger(30), 1);}
	}
}
