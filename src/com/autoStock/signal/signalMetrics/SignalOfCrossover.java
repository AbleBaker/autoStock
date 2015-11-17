package com.autoStock.signal.signalMetrics;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.indicator.IndicatorOfEMA;
import com.autoStock.indicator.results.ResultsBase;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalBaseWithPoint;
import com.autoStock.signal.SignalDefinitions.IndicatorParameters;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.trading.types.Position;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCrossover extends SignalBaseWithPoint {
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

	@Override
	public SignalPoint getSignalPoint(Position position) {
		double value = getStrength();
		
		if (hasCrossed() == false){
			return new SignalPoint();
		}
		
		if (position == null){
			if (value >= 2){return new SignalPoint(SignalPointType.long_entry, SignalMetricType.metric_crossover);}
			if (value <= -2){return new SignalPoint(SignalPointType.short_entry, SignalMetricType.metric_crossover);}
		}else{
			if (value <= 2 && position.isLong()){return new SignalPoint(SignalPointType.long_exit, SignalMetricType.metric_crossover);}
			if (value >= 0 && position.isShort()){return new SignalPoint(SignalPointType.short_exit, SignalMetricType.metric_crossover);}
		}
		
		return new SignalPoint();
	}
	
	private boolean hasCrossed(){
		for (double value : listOfNormalizedValuePersist){
			if (value >= 0 && value <= 1){
				return true;
			}
		}
		
		return false;
	}
	
	public static class IndicatorParametersForEMAFirst extends IndicatorParameters {
		public IndicatorParametersForEMAFirst() {super(new MutableInteger(10), 1);}
	}
	
	public static class IndicatorParametersForEMASecond extends IndicatorParameters {
		public IndicatorParametersForEMASecond() {super(new MutableInteger(30), 1);}
	}
}
