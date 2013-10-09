/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * @author Kevin Kowalewski
 *
 */
public class MathTools {
	public static double round(double value){
		return Math.round(value*1000.0)/1000.0;
	}
	
	public static double roundAccurate(double value){
		return Math.round(value*1000000.0)/1000000.0;
	}
	
	public static int[] averageArray(int[] arrayOfInt){
		int[] arrayOfResults = new int[arrayOfInt.length];
		
		for (int i=0; i < arrayOfInt.length; i++){
			if (i == 0){
				arrayOfResults[0] = (int) (arrayOfInt[0] + arrayOfInt[1]) / 2;
			}else if (i != arrayOfInt.length-1){
				arrayOfResults[i] = (int) (arrayOfInt[i-1] + arrayOfInt[i] + arrayOfInt[i+1]) / 3;
			}else{
				arrayOfResults[i] = (int) (arrayOfInt[i] + arrayOfInt[i-1]) / 2;
			}
		}
		
		return arrayOfResults;
	}
	
	public static double[] averageArray(double[] arrayOfDouble){
		double[] arrayOfResults = new double[arrayOfDouble.length];
		
		for (int i=0; i < arrayOfDouble.length; i++){
			if (i == 0){
				arrayOfResults[0] = (arrayOfDouble[0] + arrayOfDouble[1]) / 2;
			}else if (i != arrayOfDouble.length-1){
				arrayOfResults[i] = (arrayOfDouble[i-1] + arrayOfDouble[i] + arrayOfDouble[i+1]) / 3;
			}else{
				arrayOfResults[i] = (arrayOfDouble[i] + arrayOfDouble[i-1]) / 2;
			}
		}
		
		return arrayOfResults;
	}
	
	public static double getAverage(double[] arrayOfDouble){
		double average = 0;
		
		for (double number : arrayOfDouble){
			average += number;
		}
		
		return average /= arrayOfDouble.length;
	}
	
	public static double getAverage(int[] arrayOfInt){
		double average = 0;
		
		for (int number : arrayOfInt){
			average += number;
		}
		
		return average /= arrayOfInt.length;
	}
	
	public static float getAverage(float[] arrayOfFloat){
		float average = 0;
		
		for (float number : arrayOfFloat){
			average += number;
		}
		
		return average /= arrayOfFloat.length;
	}
	
	public static double getMax(double... numbers){
		double returnValue = Double.MIN_VALUE;
		for (double number : numbers){
			if (number > returnValue){
				returnValue = number;
			}
		}
		
		return returnValue;
	}
	
	public static double getMin(double... numbers){
		double returnValue = Double.MAX_VALUE;
		for (double number : numbers){
			if (number < returnValue){
				returnValue = number;
			}
		}
		
		return returnValue;
	}
	
	public static int getMax(int... numbers){
		int returnValue = Integer.MIN_VALUE;
		for (int number : numbers){
			if (number > returnValue){
				returnValue = number;
			}
		}
		
		return returnValue;
	}
	
	public static int getMin(int... numbers){
		int returnValue = Integer.MAX_VALUE;
		for (int number : numbers){
			if (number < returnValue){
				returnValue = number;
			}
		}
		
		return returnValue;
	}
	
	public static double getMaxDouble(ArrayList<Double> listOfDouble){
		double returnValue = Double.NEGATIVE_INFINITY;
		
		if (listOfDouble.size() == 0){return 0;}
		
		for (double number : listOfDouble){
			if (number > returnValue){
				returnValue = number;
			}
		}
		
		return returnValue;
	}
	
	public static double getMinDouble(ArrayList<Double> listOfDouble){
		double returnValue = Double.POSITIVE_INFINITY;
		
		if (listOfDouble.size() == 0){return 0;}
		
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
	
	public static boolean isIncreasing(double[] arrayOfDouble, int allowed, boolean average){
		if (arrayOfDouble.length < 2){
			throw new IllegalArgumentException("Array length must be at least 2");
		}
		
		if (arrayOfDouble.length %2 != 0){
			throw new IllegalArgumentException("Array length must be even");
		}
		
		if (average){
			arrayOfDouble = MathTools.averageArray(arrayOfDouble);
		}
		
		double[] firstHalf = Arrays.copyOfRange(arrayOfDouble, 0, arrayOfDouble.length / 2);
		double[] secondHalf = Arrays.copyOfRange(arrayOfDouble, arrayOfDouble.length / 2, arrayOfDouble.length);
		
		if (MathTools.getAverage(firstHalf) > MathTools.getAverage(secondHalf)){
			return false;
		}
		
		for (int i=0; i<arrayOfDouble.length-1; i++){
			double firstNumber = arrayOfDouble[i];
			double secondNumber = arrayOfDouble[i+1];
			
			if (firstNumber > secondNumber){
				if (allowed == 0){
					return false;
				}else{
					allowed--;
				}
			}
		}
		
		return true;
	}
	
	public static boolean isDecreasing(double[] arrayOfDouble, int allowed, boolean average){
		if (arrayOfDouble.length < 2){
			throw new IllegalArgumentException("Array length must be at least 2");
		}
		
		if (arrayOfDouble.length %2 != 0){
			throw new IllegalArgumentException("Array length must be even");
		}
		
		if (average){
			arrayOfDouble = MathTools.averageArray(arrayOfDouble);
		}
		
		double[] firstHalf = Arrays.copyOfRange(arrayOfDouble, 0, arrayOfDouble.length / 2);
		double[] secondHalf = Arrays.copyOfRange(arrayOfDouble, arrayOfDouble.length / 2, arrayOfDouble.length);
		
		if (MathTools.getAverage(firstHalf) < MathTools.getAverage(secondHalf)){
			return false;
		}
		
		for (int i=0; i<arrayOfDouble.length-1; i++){
			double firstNumber = arrayOfDouble[i];
			double secondNumber = arrayOfDouble[i+1];
			
			if (firstNumber < secondNumber){
				if (allowed == 0){
					return false;
				}else{
					allowed--;
				}
			}
		}
		
		return true;
	}
	
	public static boolean isFlat(double[] arrayOfDouble, double minVarience){
		double avg = getAverage(arrayOfDouble);
		
		if ( Math.abs(arrayOfDouble[arrayOfDouble.length-1] - avg) > minVarience){
			return false;
		}
		
		return true;
	}
	
//	public static double[] getDeltas(int[] arrayOfInt){
//		double[] returnArray = new double[arrayOfInt.length];
//		for (int i=0; i<arrayOfInt.length; i++){
//			returnArray[i] = arrayOfInt
//		}
//	}

//	public static double pow(double base, double exponent) {
//		if (base < 0) {
//			return Math.pow(Math.abs(base), exponent) * -1;
//		}
//		return Math.pow(base, exponent);
//	}
	
	public static double pow(double base, double exponent) {
		if (base < 0) {
			return powFast(Math.abs(base), exponent) * -1;
		}
		return powFast(base, exponent);
	}
	
	public static double powFast(final double a, final double b) {
	    final long tmp = Double.doubleToLongBits(a);
	    final long tmp2 = (long)(b * (tmp - 4606921280493453312L)) + 4606921280493453312L;
	    return Double.longBitsToDouble(tmp2);
	}
	
	public static boolean isOdd(int number){
		return number % 2 == 0 ? false : true;
	}
	
	public static boolean isEven(int number){
		return number % 2 == 0 ? true : false;
	}

	public static int[] getDeltas(int[] arrayOfInt) {
		int[] results = new int[arrayOfInt.length];
		
		for (int i=0; i<arrayOfInt.length-1; i++){
			results[i] = arrayOfInt[i] = arrayOfInt[i+1];
		}
		
		return results;
	}
}
