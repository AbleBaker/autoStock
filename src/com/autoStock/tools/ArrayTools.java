/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

	public static int[] getArrayFromListOfInt(ArrayList<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}
	
	public static double[] getArrayFromListOfDouble(ArrayList<Double> doubles) {
		double[] ret = new double[doubles.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = doubles.get(i).doubleValue();
		}
		return ret;
	}
	
	public static double[] convertToDouble(int[] arrayOfInt) {
		double[] ret = new double[arrayOfInt.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = Double.valueOf(arrayOfInt[i]);
		}
		return ret;
	}
	
	public static Date[] getArrayFromListOfDates(ArrayList<Date> dates){
		Date[] ret = new Date[dates.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = dates.get(i);
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static void sort2DStringArray(String[][] arrayOfString){
		Arrays.sort(arrayOfString, new Comparator() {
		    public int compare(Object o1, Object o2) {
		        String[] elt1 = (String[])o1;
		        String[] elt2 = (String[])o2;
		        return elt1[0].compareTo(elt2[0]);
		    }
		});
	}
	
	public static double getLastElement(double[] arrayOfDouble){
		return arrayOfDouble[arrayOfDouble.length-1];
	}
	
	public static double[] getDoubleArray(ArrayList<Double> listOfDouble){
		double[] arrayOfDouble = new double[listOfDouble.size()];
		
		for (int i=0; i<listOfDouble.size()-1; i++){
			arrayOfDouble[i] = listOfDouble.get(i);
		}
		
		return arrayOfDouble;
	}
}
