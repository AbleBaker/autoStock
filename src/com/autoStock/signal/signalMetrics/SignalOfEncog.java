package com.autoStock.signal.signalMetrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.PersistNEATNetwork;
import org.encog.neural.networks.PersistBasicNetwork;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.extras.EncogInputWindow;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 * 
 */
public class SignalOfEncog extends SignalBase {
	private static final double NEURON_THRESHOLD = 0.95;
	public static final int INPUT_WINDOW_PS = 5;
	public static final int INPUTS = 2;
	
	// private MLRegression basicNetwork;
	private NEATNetwork neatNetwork;
	private EncogInputWindow encogInputWindow;
	private SignalPointType lastSignalPointType = SignalPointType.none;

	public SignalOfEncog(SignalParameters signalParameters) {
		super(SignalMetricType.metric_encog, signalParameters);

		try {
			PersistBasicNetwork persistBasicNetwork = new PersistBasicNetwork();
			// basicNetwork = (BasicNetwork) persistBasicNetwork.read(new
			// FileInputStream(new File("../EncogTest/encog.file")));
		} catch (Exception e) {
		}

		PersistNEATNetwork persistBasicNetwork = new PersistNEATNetwork();
		try {
			neatNetwork = (NEATNetwork) persistBasicNetwork.read(new FileInputStream(new File("encog.file")));
//			 Co.println("--> Read network...");
		} catch (FileNotFoundException e) {
			// Co.println("--> Warning, encog network definition not found");
		}
	}

	public void setNetwork(MLRegression network) {
		// this.basicNetwork = network;
		neatNetwork = (NEATNetwork) network;
	}

	@Override
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
		SignalPoint signalPoint = new SignalPoint();

		if (encogInputWindow == null || neatNetwork == null) {
			return signalPoint;
		}

		NormalizedField normalizedFieldForSignals = new NormalizedField(NormalizationAction.Normalize, "Signal Normalizer", 50, -50, 1, -1);

		MLData input = new BasicMLData(neatNetwork.getInputCount());

		double[] inputWindow = encogInputWindow.getAsWindow();

		if (inputWindow.length != neatNetwork.getInputCount()) {
			throw new IllegalArgumentException("Input sizes don't match, supplied, needed: " + inputWindow.length + ", " + neatNetwork.getInputCount());
		}

		for (int i = 0; i < inputWindow.length; i++) {
			// Co.print(" " + inputWindow[i]);
			input.add(i, normalizedFieldForSignals.normalize(inputWindow[i]));
		}

		MLData output = neatNetwork.compute(input);

		double valueForLongEntry = output.getData(0);
		double valueForShortEntry = output.getData(1);
		double valueForAnyExit = output.getData(2);
		
//		Co.println("--> Values: " + valueForLongEntry + ", " + valueForShortEntry + ", " + valueForAnyExit);

		if (valueForLongEntry >= NEURON_THRESHOLD) {
			// Co.println("--> Long entry?");
			signalPoint.signalPointType = SignalPointType.long_entry;
			signalPoint.signalMetricType = SignalMetricType.metric_encog;
			// }
		} else if (valueForShortEntry >= NEURON_THRESHOLD) {
			signalPoint.signalPointType = SignalPointType.short_entry;
			signalPoint.signalMetricType = SignalMetricType.metric_encog;
		} else if (valueForAnyExit >= NEURON_THRESHOLD && havePosition) {
			if (positionType == PositionType.position_long) {
				signalPoint.signalPointType = SignalPointType.long_exit;
			} else if (positionType == PositionType.position_short) {
				signalPoint.signalPointType = SignalPointType.short_exit;
			} else {
				throw new IllegalStateException("Have position of type: " + positionType.name());
			}
			signalPoint.signalMetricType = SignalMetricType.metric_encog;
		}

		// if (signalPoint.signalPointType == SignalPointType.long_entry ||
		// signalPoint.signalPointType == SignalPointType.long_exit){
		// Co.println("--> Encog signal with window: " +
		// signalPoint.signalPointType.name());
		// for (int i=0; i<inputWindow.length; i++){
		// Co.print(" " + inputWindow[i]);
		// }
		// }

		return signalPoint;
		// Co.println("--> " + valueForEntry + ", " + valueForExit);

		//if (signalPoint.signalPointType != lastSignalPointType) {
		//	lastSignalPointType = signalPoint.signalPointType;
		//} else {
		//	return new SignalPoint(SignalPointType.none, SignalMetricType.metric_encog);
		//}

	}

	public void setInput(EncogInputWindow encogInputWindow) {
		this.encogInputWindow = encogInputWindow;
	}
	
	public double[] getInputWindowRounded(){
		if (encogInputWindow == null){return null;}
		double[] array = new double[encogInputWindow.getAsWindow().length];
		
		for (int i=0; i<array.length; i++){
			array[i] = MathTools.round(encogInputWindow.getAsWindow()[i]);
		}
		
		return array;
	}
	
	public EncogInputWindow getInputWindow() {
		return encogInputWindow;
	}
	
	public static int getInputWindowLength(){
		return INPUT_WINDOW_PS * INPUTS;
	}

	@Override
	public void setInput(double value) {
		throw new NoSuchMethodError("Don't call this");
	}
	
	@Override
	public void reset() {
		super.reset();
		encogInputWindow = null;
	}

	public boolean isLongEnough(SignalBase... arrayOfSignalBase) {
		for (SignalBase signalBase : arrayOfSignalBase){
			if (signalBase.listOfNormalizedValuePersist.size() <= INPUT_WINDOW_PS){
				return false;
			}
		}
		
		return true;
	}
}
