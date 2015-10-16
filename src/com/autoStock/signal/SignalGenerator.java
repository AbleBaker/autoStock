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
		
		FrameType frameType = FrameType.percent_change;
		
		SignalBase[] arrayOfSignalBase = {signalGroup.signalOfCCI,
										  signalGroup.signalOfUO,
//										  signalGroup.signalOfWILLR,
//										  signalGroup.signalOfADX,
										  signalGroup.signalOfDI,
//										  signalGroup.signalOfROC
										  };
		
		
		if (signalGroup.signalOfEncog.isLongEnough(arrayOfSignalBase)){
//			Co.println("--> Trying to use Encog!");
			
			EncogInputWindow encogWindow = new EncogInputWindow();
			EncogFrame encogFrame = new EncogFrame("Typical indicators", frameType);
			
			for (SignalBase signalBase : arrayOfSignalBase){
				EncogSubframe subframe = getSubFrame(signalBase, frameType);
				for (Double value : subframe.asDoubleList()){
					if (Double.isNaN(value)){ // || Double.isInfinite(value)){
						throw new IllegalStateException("Subframe value was NaN or Infinite: " + subframe.frameType.name() + ", " + signalBase.getClass().getName() + ", " + value);
					}
				}
				encogFrame.addSubframe(subframe);
			}
			
			if (encogFrames != null){encogWindow.addFrames(encogFrames);}
			
			encogWindow.addFrame(encogFrame);

			signalGroup.signalOfEncog.setInput(encogWindow);
		}
	}
	
	public EncogSubframe getSubFrame(SignalBase signalBase, FrameType frameType){
		EncogSubframe subFrame = null;
		
		if (frameType == FrameType.percent_change){
			subFrame = new EncogSubframe(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalBase.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1), frameType);
		}if (frameType == FrameType.delta_change){
			subFrame = new EncogSubframe(Arrays.copyOfRange(MathTools.getDeltas(signalBase.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1), frameType);
		}else if (frameType == FrameType.raw){
			subFrame = new EncogSubframe(Arrays.copyOfRange(signalBase.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1), 1, SignalOfEncog.INPUT_WINDOW_PS + 1), frameType);
		}
		
		if (subFrame != null){return subFrame;}
		else {throw new IllegalStateException("Can't handle type: " + frameType.name());}
	}
}
