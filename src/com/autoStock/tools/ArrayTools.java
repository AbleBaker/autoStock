/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Kevin Kowalewski
 * 
 */
public class ArrayTools {
	public static float[] shiftArrayDown(float[] array, int shift) {
		float[] arrayOfFloat = new float[array.length];

		for (int i = shift; i < array.length; i++) {
			arrayOfFloat[i] = array[i - shift];
		}

		return arrayOfFloat;
	}

	public static double[] shiftArrayDown(double[] array, int shift) {
		double[] arrayOfdouble = new double[array.length];

		for (int i = shift; i < array.length; i++) {
			arrayOfdouble[i] = array[i - shift];
		}

		return arrayOfdouble;
	}

	public static double[] subArray(double[] array, int start, int end) {
		double[] arrayOfDouble = new double[end - start];

		for (int i = start; i < end; i++) {
			arrayOfDouble[i - start] = array[i];
		}

		return arrayOfDouble;
	}

	public static int[] convertIntegers(ArrayList<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}
	
	public static double[] convertDoubles(ArrayList<Double> doubles) {
		double[] ret = new double[doubles.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = doubles.get(i).doubleValue();
		}
		return ret;
	}
	
	public static Date[] convertDates(ArrayList<Date> dates){
		Date[] ret = new Date[dates.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = dates.get(i);
		}
		return ret;
	}

}
