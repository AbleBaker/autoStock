/**
 * 
 */
package com.autoStock.signal;

import java.util.Arrays;

import com.autoStock.signal.extras.EncogInputWindow;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin
 *
 */
public class SignalGenerator {
	public void generateEncogSignal(SignalGroup signalGroup){
		if (signalGroup.signalOfEncog.isLongEnough(signalGroup.signalOfCCI, signalGroup.signalOfUO, signalGroup.signalOfWILLR)){
//			Co.println("--> Trying to use Encog!");
			
			EncogInputWindow encogWindow = new EncogInputWindow();
			
//			Co.println("--> A: " + PrintTools.getString(signalOfCCI.getNormalizedWindow(4)));
			
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalGroup.signalOfCCI.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalGroup.signalOfUO.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalGroup.signalOfWILLR.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfSAR.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));

			//encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfARUp.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			//encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfARDown.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			
//			Co.println("--> B: " + encogWindow.getAsWindow().length);
			
//			Co.println(PrintTools.getString(encogWindow.getAsWindow()));
			
//			Co.println("--> C");
			
//			Co.println(PrintTools.getStringFromArray(MathTools.getDeltasAsPercent(signalOfCCI.getStrengthWindow())));
			
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfCCI.getStrengthWindow()), 1, 5));
			
			
//			encogWindow.addInput(signalOfUO.getStrength());
////			encogWindow.addInput(signalOfCCI.getStrength());
//			encogWindow.addInput(signalOfARUp.getStrength());
//			encogWindow.addInput(signalOfARDown.getStrength());
////			encogWindow.addInput(signalOfTRIX.getStrength());
			
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfUO.getStrengthWindow()), 0, ENCOG_SIGNAL_INPUT));
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfTRIX.getStrengthWindow()), 0, ENCOG_SIGNAL_INPUT));
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfARUp.getStrengthWindow()), 0, ENCOG_SIGNAL_INPUT));
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfARDown.getStrengthWindow()), 0, ENCOG_SIGNAL_INPUT));
//			encogWindow.addInputList(signalOfRSI.listOfNormalizedValue.subList(signalOfRSI.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfRSI.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfUO.listOfNormalizedValue.subList(signalOfUO.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfUO.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfARUp.listOfNormalizedValue.subList(signalOfARUp.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfARUp.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfARDown.listOfNormalizedValue.subList(signalOfARDown.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfARDown.listOfNormalizedValue.size()));
//			
//			
//			Co.println("\n");
//			
//			for (int i=0; i<signalOfUO.getStrengthWindow().length; i++){
//				Co.print(" " + signalOfUO.getStrengthWindow()[i]);
//			}
//			
//			Co.print(" | ");
//			
//			for (int i=0; i<encogWindow.getAsWindow().length; i++){
//				Co.print(" " + encogWindow.getAsWindow()[i]);
//			}
			
			signalGroup.signalOfEncog.setInput(encogWindow);
		}
	}
}
