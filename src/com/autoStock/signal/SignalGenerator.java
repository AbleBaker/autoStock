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
		if (signalGroup.signalOfEncog.isLongEnough(signalGroup.signalOfCCI, signalGroup.signalOfUO, signalGroup.signalOfWILLR, signalGroup.signalOfADX, signalGroup.signalOfDI, signalGroup.signalOfROC)){
//			Co.println("--> Trying to use Encog!");
			
			EncogInputWindow encogWindow = new EncogInputWindow();
			
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalGroup.signalOfCCI.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalGroup.signalOfUO.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalGroup.signalOfWILLR.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalGroup.signalOfADX.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalGroup.signalOfDI.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalGroup.signalOfROC.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
		
			signalGroup.signalOfEncog.setInput(encogWindow);
			
		}
	}
}
