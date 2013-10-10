package com.autoStock.signal;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.PersistBasicNetwork;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import com.autoStock.Co;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.tools.ListTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfEncog extends SignalBase {
	private MLRegression basicNetwork;
	private EncogInputWindow encogInputWindow;
	private SignalPointType lastSignalPointType = SignalPointType.none;
	
	public SignalOfEncog(SignalParameters signalParameters) {
		super(SignalMetricType.metric_encog, signalParameters);
		
		try {
			PersistBasicNetwork persistBasicNetwork = new PersistBasicNetwork();
			basicNetwork = (BasicNetwork) persistBasicNetwork.read(new FileInputStream(new File("../EncogTest/encog.file")));
		}catch(Exception e){
//			e.printStackTrace();
		}
	}
	

	public void setNetwork(MLRegression network) {
		this.basicNetwork = network;
	}
	
	@Override
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
		SignalPoint signalPoint = new SignalPoint();
		
		if (encogInputWindow == null){return signalPoint;}
		
		NormalizedField normalizedFieldForSignals = new NormalizedField(NormalizationAction.Normalize, "Signal Normalizer", 50, -50, 1, -1);
		
		MLData input = new BasicMLData(basicNetwork.getInputCount());
		
		int[] inputWindow = encogInputWindow.getAsWindow();
		
//		if (inputWindow.length != basicNetwork.getInputCount()){
//			throw new IllegalArgumentException("Input sizes don't match, supplied, needed: " + inputWindow.length + ", " + basicNetwork.getInputCount());
//		}		
		
//		Co.println("--> Inputs... ");
		
		if (havePosition){
			input.add(0, 1);
		}else{
			input.add(0, -1);
		}
		
		for (int i=0; i<inputWindow.length; i++){
//			Co.print(" " + inputWindow[i]);
			input.add(i+1, normalizedFieldForSignals.normalize(inputWindow[i]));
		}
        
        MLData output = basicNetwork.compute(input);
        
        double valueForLongEntry = output.getData(0);
        double valueForShortEntry = output.getData(1);
        double valueForAnyExit = output.getData(2);
        
        if (valueForLongEntry >= 0.99){
        	signalPoint.signalPointType = SignalPointType.long_entry;
        	signalPoint.signalMetricType = SignalMetricType.metric_encog;
        }else if (valueForShortEntry >= 0.99){
        	signalPoint.signalPointType = SignalPointType.short_entry;
        	signalPoint.signalMetricType = SignalMetricType.metric_encog;
        }else if (valueForAnyExit >= 0.99 && havePosition){
        	if (positionType == PositionType.position_long){
        		signalPoint.signalPointType = SignalPointType.long_exit;
        	}else if (positionType == PositionType.position_short){
        		signalPoint.signalPointType = SignalPointType.short_exit;
        	}else{
        		throw new IllegalStateException("Have position of type: " + positionType.name());
        	}
        	signalPoint.signalMetricType = SignalMetricType.metric_encog;
        }
        
        
//        if (signalPoint.signalPointType == SignalPointType.long_entry || signalPoint.signalPointType == SignalPointType.long_exit){
//        	Co.println("--> Encog signal with window: " + signalPoint.signalPointType.name());
//        	for (int i=0; i<inputWindow.length; i++){
//        		Co.print(" " + inputWindow[i]);
//        	}
//        }
        
//        Co.println("--> " + valueForEntry + ", " + valueForExit);
        
        if (signalPoint.signalPointType != lastSignalPointType){
        	lastSignalPointType = signalPoint.signalPointType;
        	return signalPoint;        	
        }else{
        	return new SignalPoint(SignalPointType.none, SignalMetricType.metric_encog);
        }
        
	}
	
	public void setInput(EncogInputWindow encogInputWindow){
		this.encogInputWindow = encogInputWindow;
	}
	
	@Override
	public void setInput(double value) {
		throw new NoSuchMethodError("Don't call this");
	}
	
	public static class EncogInputWindow {
		private List<List<Integer>> list = new ArrayList<List<Integer>>();
		
		public EncogInputWindow(){}
		
		public void addInputList(List<Integer> listOfInteger){
			list.add(listOfInteger);
		}
		
		public void addInputArray(int[] arrayOfInput){
			list.add((ArrayList<Integer>) ListTools.getListFromArray(arrayOfInput));
		}
		
		public int[] getAsWindow(){
			int items = 0;
			
			for (List<?> arrayList : list){
				for (Object object : arrayList){
					items++;
				}
			}
			
			int[] arrayOfInt = new int[items];
			int i=0;
			
			for (List<Integer> arrayList : list){
				for (Integer integer : arrayList){
					arrayOfInt[i] = integer;
					i++;
				}
			}
			
			return arrayOfInt;
		}
	}
}
