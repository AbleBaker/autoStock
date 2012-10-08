/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;


/**
 * @author Kevin Kowalewski
 *
 */
public class MathTools {
	public static double round(double value){
		return Math.round(value*1000.0)/1000.0;
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
	
	public static double getMaxDouble(ArrayList<Double> listOfDouble){
		double returnValue = Double.MIN_VALUE;
		for (double number : listOfDouble){
			if (number > returnValue){
				returnValue = number;
			}
		}
		
		return returnValue;
	}
	
	public static double getMinDouble(ArrayList<Double> listOfDouble){
		double returnValue = Double.MAX_VALUE;
		for (double number : listOfDouble){
			if (number < returnValue){
				returnValue = number;
			}
		}
		
		return returnValue;
	}
	
	public static int getMaxInt(ArrayList<Integer> listOfInteger){
		int returnValue = Integer.MIN_VALUE;
		for (int number : listOfInteger){
			if (number > returnValue){
				returnValue = number;
			}
		}
		
		return returnValue == Integer.MIN_VALUE ? 0 : returnValue;
	}
	
	public static int getMinInt(ArrayList<Integer> listOfInteger){
		int returnValue = Integer.MAX_VALUE;
		for (int number : listOfInteger){
			if (number < returnValue){
				returnValue = number;
			}
		}
		
		return returnValue == Integer.MAX_VALUE ? 0 : returnValue;
	}
	
	public static boolean isOdd(int number){
		return number % 2 == 0 ? false : true;
	}
	
	public static boolean isEven(int number){
		return number % 2 == 0 ? true : false;
	}
}
