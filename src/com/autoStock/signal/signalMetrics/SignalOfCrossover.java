package com.autoStock.signal.signalMetrics;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.indicator.IndicatorOfEMA;
import com.autoStock.indicator.results.ResultsBase;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalBaseWithPoint;
import com.autoStock.signal.SignalDefinitions.IndicatorParametersForEMAFirst;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalDefinitions.SignalParametersForCrossover;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCrossover extends SignalBaseWithPoint {
	public SignalOfCrossover(SignalMetricType signalMetricType, SignalParameters signalParameters, AlgorithmBase algorithmBase) {
		super(signalMetricType, signalParameters, algorithmBase);
	}
	
	@Override
	public void setInput(ResultsBase resultsBase) {
		setInput(0);
	}

	@Override
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
		
//		IndicatorOfEMA ema1 = new IndicatorOfEMA(new IndicatorParametersForEMAFirst(), , taLibCore, SignalMetricType.metric_crossover));
//		InficatorOfEMA ema2 = new IndicatorOfEMA(new IndicatorParametersForEMAFirst(), commonAnalysisData, taLibCore, SignalMetricType.metric_crossover));
//		
		
		
//		double currentGap = getStrength();
//		
//		if (currentGap >= ((SignalParametersForCrossover)signalParameters).longGapSize.value && havePosition == false){
//			return new SignalPoint(SignalPointType.long_entry, signalMetricType);
//		}else if (currentGap <= 0 && havePosition && positionType == PositionType.position_long){
//			return new SignalPoint(SignalPointType.long_exit, signalMetricType);
//		}else if (currentGap >= 0 && havePosition && positionType == PositionType.position_short){
//			return new SignalPoint(SignalPointType.short_exit, signalMetricType);
//		}else if (currentGap <= ((SignalParametersForCrossover)signalParameters).shortGapSize.value && havePosition == false){
//			return new SignalPoint(SignalPointType.short_entry, signalMetricType);
//		}

		return new SignalPoint();
	}
}
