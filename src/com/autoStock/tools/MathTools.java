/**
 * 
 */
package com.autoStock.tools;


/**
 * @author Kevin Kowalewski
 *
 */
public class MathTools {
	public static String round(double value){
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
	
	public static double max(double... numbers){
		double returnValue = Double.MIN_VALUE;
		for (double number : numbers){
			if (number > returnValue){
				returnValue = number;
			}
		}
		
		return returnValue;
	}
	
	public static double min(double... numbers){
		double returnValue = Double.MAX_VALUE;
		for (double number : numbers){
			if (number < returnValue){
				returnValue = number;
			}
		}
		
		return returnValue;
	}
}
