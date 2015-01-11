package com.autoStock.signal.signalMetrics;

import java.util.ArrayList;

import org.encog.ml.MLMethod;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import com.autoStock.Co;
import com.autoStock.backtest.encog.TrainEncogBase;
import com.autoStock.backtest.encog.TrainEncogNetworkOfBasic;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalDefinitions.SignalParametersForEncog;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalPoint;
import com.autoStock.signal.extras.EncogFrame;
import com.autoStock.signal.extras.EncogInputWindow;
import com.autoStock.signal.extras.EncogNetworkCache;
import com.autoStock.signal.extras.EncogNetworkGenerator;
import com.autoStock.signal.extras.EncogNetworkProvider;
import com.autoStock.signal.extras.EncogFrame.FrameType;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.ListTools;

/**
 * @author Kevin Kowalewski
 * 
 */
public class SignalOfEncogNew extends SignalBase {
	public static final double NORM_MAX = 10000;
	public static final double NORM_MIN = -10000;
	private static final double NEURON_THRESHOLD = 0.95;
	private EncogInputWindow encogInputWindow;
	private EncogNetworkProvider encogNetworkProvider = new EncogNetworkProvider();
	private ArrayList<EncogNetwork> listOfNetworks = new ArrayList<EncogNetwork>();
	private NormalizedField normalizerSmall = new NormalizedField(NormalizationAction.Normalize, "Small Normalizer", 100, -100, 1, -1);
	private NormalizedField normalizerLarge = new NormalizedField(NormalizationAction.Normalize, "Large Normalizer", NORM_MAX, NORM_MIN, 1, -1);
	private String networkName;
	
	public static class EncogNetwork {
		public String description;
		public MLMethod method;
		public ArrayList<EncogFrame> listOfFrame = new ArrayList<EncogFrame>();
		
		public EncogNetwork(MLMethod method, String description, EncogFrame encogFrame) {
			this.method = method;
			this.description = description;
			listOfFrame.add(encogFrame);
		}
		
		public EncogNetwork(MLMethod method, String description) {
			this.description = description;
			this.method = method;
		}
		
		@Override
		public String toString() {
			return description + " : " + ((BasicNetwork)method).getInputCount() + ", " + ((BasicNetwork)method).getOutputCount();
		}
	}
	
	public SignalOfEncogNew(SignalParametersForEncog signalParameters) {
		super(SignalMetricType.metric_encog, signalParameters);
	}

	public void setInput(EncogInputWindow encogInputWindow) {
		if (encogInputWindow == null){throw new IllegalArgumentException("Can't set a null EncogInputWindow");}
		this.encogInputWindow = encogInputWindow;
		
		if (listOfNetworks.size() == 0){
			setup();
		}
	}
	
	public void setNetworkName(String networkName){
		this.networkName = networkName;
		readFromDisk();
	}
	
	public void setNetwork(MLRegression network, int which){
		Co.println("--> Set network at/with: " + which + ", " + ((BasicNetwork)network).getInputCount());
		listOfNetworks.get(which).method = network;
	}
	
	public void setNetwork(MLRegression network){
		throw new IllegalAccessError("Don't call this");
	}
	
	public void setup(){
//		for (EncogFrame encogFrame : encogInputWindow.getFrames()){
//			new EncogNetwork(EncogNetworkGenerator.getBasicNetwork(encogFrame.getLength(), outputs), null);
//		}
		
		listOfNetworks.add(new EncogNetwork(EncogNetworkGenerator.getBasicNetwork(6, 4), "Network 0"));
		listOfNetworks.add(new EncogNetwork(EncogNetworkGenerator.getBasicNetwork(152, 1), "Network 1"));
		listOfNetworks.add(new EncogNetwork(EncogNetworkGenerator.getBasicNetwork(16, 1), "Network 2"));
		listOfNetworks.add(new EncogNetwork(EncogNetworkGenerator.getBasicNetwork(120, 4), "Network 3"));
	}
	
	public void readFromDisk(){
//		EncogNetwork network0Cached = EncogNetworkCache.getInstance().get(networkName + "-0", true);
//		EncogNetwork network0 = new EncogNetwork(encogNetworkProvider.getBasicNetwork(networkName + "-0"), "Network 0");
//		if (network0 != null){listOfNetworks.add(network0); EncogNetworkCache.getInstance().put(networkName + "-0", network0);}
//		
//		try {listOfNetworks.add(new EncogNetwork(encogNetworkProvider.getBasicNetwork(networkName + "-1"), "Network 1"));}catch(Exception e){Co.println("--> Network not found when reading from disk");}
//		try {listOfNetworks.add(new EncogNetwork(encogNetworkProvider.getBasicNetwork(networkName + "-2"), "Network 2"));}catch(Exception e){Co.println("--> Network not found when reading from disk");}
//		try {listOfNetworks.add(new EncogNetwork(encogNetworkProvider.getBasicNetwork(networkName + "-3"), "Network 3"));}catch(Exception e){Co.println("--> Network not found when reading from disk");}
		
		readAndStoreNetwork(networkName + "-0");
		readAndStoreNetwork(networkName + "-1");
		readAndStoreNetwork(networkName + "-2");
		readAndStoreNetwork(networkName + "-3");
	}
	
	public void readAndStoreNetwork(String networkName){
		BasicNetwork basicNetworkCached = (BasicNetwork) EncogNetworkCache.getInstance().get(networkName, true);
		if (basicNetworkCached != null){
			listOfNetworks.add(new EncogNetwork(basicNetworkCached, networkName));
		}else{
			EncogNetwork encogNetwork = new EncogNetwork(encogNetworkProvider.getBasicNetwork(networkName), networkName);
			EncogNetworkCache.getInstance().put(networkName, encogNetwork.method);
			listOfNetworks.add(encogNetwork);
		}
	}
	
	public ArrayList<EncogNetwork> getNetworks(){
		return listOfNetworks;
	}

	
	@Override
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
		SignalPoint signalPoint = new SignalPoint();
		
		if (encogInputWindow == null){
			return signalPoint;
		}
		
		BasicNetwork mainNetwork = (BasicNetwork) listOfNetworks.get(0).method;
		ArrayList<Double> inputFromNetworks = new ArrayList<Double>();
		
		int index = 1;
		
		for (EncogFrame encogFrame : encogInputWindow.getFrames()){
			EncogNetwork network = listOfNetworks.get(index);
			BasicMLData input = new BasicMLData(encogFrame.getLength());

			//Co.println("--> Input: " + network.description + ", " + encogFrame.description + ", " + encogFrame.frameType.name());
			
			if (encogFrame.frameType == FrameType.raw){
				for (int i=0; i<encogFrame.getLength(); i++){
					input.add(i, normalizerSmall.normalize(encogFrame.asDoubleArray()[i]));
				}
			}else if (encogFrame.frameType == FrameType.percent_change){
				for (int i=0; i<encogFrame.getLength(); i++){
					input.add(i, normalizerSmall.normalize(encogFrame.asDoubleArray()[i]));
				}
			}else{
				throw new IllegalArgumentException("Can't handle frame type: " + encogFrame.description + ", " + encogFrame.frameType.name());
			}
			
//			for (Double value : input.getData()){
//				Co.print(" " + value);
//			}
//			Co.println("\n");
//			
			MLData output = ((BasicNetwork)network.method).compute(input);
//			
//			Co.println("--> Output from network at/is: " + index + ", " + output.size());
//			
//			for (Double value : output.getData()){
//				Co.print(" " + value);
//			}
//			
//			Co.println("\n");
			
			inputFromNetworks.addAll(ListTools.getListFromArray(output.getData()));
			index++;
		}
		
		MLData mainInput = new BasicMLData(ArrayTools.getArrayFromListOfDouble(inputFromNetworks));
		MLData output = mainNetwork.compute(mainInput);
		
//		Co.println("--> Input to main is: ");
//		for (Double value : mainInput.getData()){
//			Co.print(" " + value);
//		}
//		Co.println("\n");
		
		//Co.println("--> Output from main is: ");
		for (Double value : output.getData()){
			//Co.print(" " + value);
			if (value > NEURON_THRESHOLD){
//				throw new IllegalAccessError();
//				Co.println("--> Cool");
			}
		}
//		Co.println("\n");

		double valueForLongEntry = output.getData(0);
		double valueForShortEntry = output.getData(1);
		double valueForAnyExit = output.getData(2);
		double valueForReeentry = output.getData(3);
		
//		Co.println("--> Values: " + valueForLongEntry + ", " + valueForShortEntry + ", " + valueForAnyExit);

		if (valueForLongEntry >= NEURON_THRESHOLD) {
			signalPoint.signalPointType = SignalPointType.long_entry;
			signalPoint.signalMetricType = SignalMetricType.metric_encog;
		} else if (valueForShortEntry >= NEURON_THRESHOLD) {
			signalPoint.signalPointType = SignalPointType.short_entry;
			signalPoint.signalMetricType = SignalMetricType.metric_encog;
		} else if (valueForReeentry >= NEURON_THRESHOLD) {
			signalPoint.signalPointType = SignalPointType.reentry;
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
		
		return signalPoint;
	}
	
	public boolean isLongEnough(SignalBase... arrayOfSignalBase) {
		for (SignalBase signalBase : arrayOfSignalBase){
			if (signalBase.listOfNormalizedValuePersist.size() <= 20){
				return false;
			}
		}
		
		return true;
	}
}
