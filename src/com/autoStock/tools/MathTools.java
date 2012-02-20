/**
 * 
 */
package com.autoStock.tools;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 *
 */
public class MathTools {
	public static String roundToTwoDecimalPlaces(double value){
		return String.valueOf(Math.round(value*100.0)/100.0);
	}
	
	public static double getAverage(double[] arrayOfDouble){
		double average = 0;
		
		for (double number : arrayOfDouble){
			average += number;
		}
		
		return average /= arrayOfDouble.length;
	}
	
	public static float getAverage(float[] arrayOfFloat){
		float average = 0;
		
		for (float number : arrayOfFloat){
			average += number;
		}
		
		return average /= arrayOfFloat.length;
	}
}
