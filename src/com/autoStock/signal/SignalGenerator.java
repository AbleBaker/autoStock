/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;
import java.util.Arrays;

import com.autoStock.signal.extras.EncogFrame;
import com.autoStock.signal.extras.EncogFrame.FrameType;
import com.autoStock.signal.extras.EncogInputWindow;
import com.autoStock.signal.extras.EncogSubframe;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin
 *
 */
public class SignalGenerator {
	public void generateEncogSignal(SignalGroup signalGroup, ArrayList<EncogFrame> encogFrames){
		
		SignalBase[] arrayOfSignalBase = {signalGroup.signalOfCCI,
										  signalGroup.signalOfUO,
										  signalGroup.signalOfWILLR,
										  signalGroup.signalOfADX,
										  signalGroup.signalOfDI,
										  signalGroup.signalOfROC};
		
		
		if (signalGroup.signalOfEncog.isLongEnough(arrayOfSignalBase)){
//			Co.println("--> Trying to use Encog!");
			
			EncogInputWindow encogWindow = new EncogInputWindow();
			EncogFrame encogFrame = new EncogFrame("Typical indicators", FrameType.percent_change);
			
			for (SignalBase signalBase : arrayOfSignalBase){
				encogFrame.addSubframe(getSubFrame(signalBase, FrameType.percent_change));
			}
			
			if (encogFrames != null){encogWindow.addFrames(encogFrames);}
			encogWindow.addFrame(encogFrame);

			signalGroup.signalOfEncog.setInput(encogWindow);
		}
	}
	
	public EncogSubframe getSubFrame(SignalBase signalBase, FrameType frameType){
		if (frameType == FrameType.percent_change){
			return new EncogSubframe(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalBase.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1), frameType);
		}else if (frameType == FrameType.raw){
			return new EncogSubframe(Arrays.copyOfRange(signalBase.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1), 1, SignalOfEncog.INPUT_WINDOW_PS + 1), frameType);
		}
		
		throw new IllegalStateException("Can't handle type: " + frameType.name());
	}
}
