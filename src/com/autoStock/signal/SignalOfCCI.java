/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.Co;
import com.autoStock.encog.TestEncogTactic;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalOfCCI extends SignalBase {	
	public SignalOfCCI(){
		super(SignalMetricType.metric_cci);
	}
	
//	@Override
//	public SignalPoint getSignalPoint(boolean havePosition, PositionType positionType) {
//		int size = listOfNormalizedAveragedValue.size();
//		
//		
//		
//		if (size >= 3){
//			TestEncogTactic testEncogTactic = new TestEncogTactic();
//			
//			int[] cciWindow = new int[3];
//			cciWindow[0] = listOfNormalizedAveragedValue.get(size-3);
//			cciWindow[1] = listOfNormalizedAveragedValue.get(size-2);
//			cciWindow[2] = listOfNormalizedAveragedValue.get(size-1);
//			
////			Co.println("--> Have value: " + cciWindow[0] + ", " + cciWindow[1] + ", " + cciWindow[2]);
//			
////			System.exit(0);
//			
//			return testEncogTactic.getSignalPoint(cciWindow);
//		}
//		
//		return new SignalPoint();
//	}
}
