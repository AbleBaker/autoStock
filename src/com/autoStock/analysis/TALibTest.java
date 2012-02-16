/**
 * 
 */
package com.autoStock.analysis;

import com.autoStock.Co;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class TALibTest {
	public void test(){
		Core taLibCore = new Core();
		
		double[] values = new double[]{1,2,3,4,5,6,7,8,9,10};
		double[] returnedValuesA = new double[10];
		double[] returnedValuesB = new double[10];
		double[] returnedValuesC = new double[10];
//		
//		taLibCore.wma(0, 9, values, 2, new MInteger(), new MInteger(), returnedValues);
//		
//		Co.println("Values back: ");
//		
	
		RetCode retCode = taLibCore.bbands(0, 9, values, 3, 1, 10, MAType.Kama, new MInteger(), new MInteger(), returnedValuesA, returnedValuesB, returnedValuesC);
		Co.println(retCode.name());
		
		for (double value : returnedValuesB){
			Co.println("Value: " + value);
		}
	}
}
