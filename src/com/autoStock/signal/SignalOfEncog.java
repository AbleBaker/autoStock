package com.autoStock.signal;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.PersistBasicNetwork;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

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
	private BasicNetwork basicNetwork;
	private EncogInputWindow encogInputWindow;
	
	public SignalOfEncog(SignalParameters signalParameters) {
		super(SignalMetricType.metric_encog, signalParameters);
		
		try {
			PersistBasicNetwork persistBasicNetwork = new PersistBasicNetwork();
			basicNetwork = (BasicNetwork) persistBasicNetwork.read(new FileInputStream(new File("../EncogTest/encog.file")));
		}catch(Exception e){
//			e.printStackTrace();
		}
	}

	
	@Override
	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
		SignalPoint signalPoint = new SignalPoint();
		
		if (encogInputWindow == null){return signalPoint;}
		
		NormalizedField normalizedFieldForSignals = new NormalizedField(NormalizationAction.Normalize, "Signal Normalizer", 50, -50, 1, -1);
		
		MLData input = new BasicMLData(basicNetwork.getInputCount());
		
		int[] inputWindow = encogInputWindow.getAsWindow();
		
		if (inputWindow.length != basicNetwork.getInputCount()){
			throw new IllegalArgumentException("Input sizes don't match, supplied, needed: " + inputWindow.length + ", " + basicNetwork.getInputCount());
		}
		
//		Co.println("--> Inputs... ");
		
		for (int i=0; i<inputWindow.length; i++){
//			Co.print(" " + inputWindow[i]);
			input.add(i, normalizedFieldForSignals.normalize(inputWindow[i]));
		}
		
//		System.exit(0);
        
        MLData output = basicNetwork.compute(input);
        
        double valueForEntry = output.getData(0);
        double valueForExit = output.getData(1);
        
        if (valueForEntry >= 0.99){
        	signalPoint.signalPointType = SignalPointType.long_entry;
        	signalPoint.signalMetricType = SignalMetricType.none;
        }else if (valueForExit >= 0.99){
        	signalPoint.signalPointType = SignalPointType.long_exit;
        	signalPoint.signalMetricType = SignalMetricType.none;
        }
        
//        Co.println("--> " + valueForEntry + ", " + valueForExit);
        
        return signalPoint;
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
//		public EncogInputWindow(List<?>[] arrayOfInputArrayList){
//			list = arrayOfInputArrayList;
//		}
		
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
