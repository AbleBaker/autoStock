/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Kevin Kowalewski
 *
 */
public class ListTools {
	public static void removeDuplicates(ArrayList<?> arrayList){
		Set setOfObject = new LinkedHashSet(arrayList);
		arrayList.clear();
		arrayList.addAll(setOfObject);
	}
	
	public static ArrayList<Object> mergeLists(ArrayList<Object>... listOfArrayLists){
		ArrayList<Object> listOfObject = new ArrayList<Object>();
		
		for (ArrayList<Object> list : listOfArrayLists){
			listOfObject.addAll(list);
		}
		
		return listOfObject;
	}
	
	public static ArrayList<?> reverseList(ArrayList<?> listOfObject){
		ArrayList<Object> listOfReturnObject = new ArrayList<Object>();
		
		for (int i=listOfObject.size()-1; i>=0; i--){
			listOfReturnObject.add(listOfObject.get(i));
		}
		
		return listOfReturnObject;
	}
	
	public static ArrayList<String> getArrayListFromString(String input, String separator){
		return new ArrayList<String>(Arrays.asList(input.split(separator)));
	}

	public static Collection<? extends Integer> getListFromArray(int[] arrayOfInt) {
		ArrayList<Integer> listOfInteger = new ArrayList<Integer>();
		for (int integer : arrayOfInt){
			listOfInteger.add(new Integer(integer));	
		}
		
		return listOfInteger;
	}

	public static Collection<? extends Double> getListFromArray(double[] arrayOfDouble) {
		ArrayList<Double> listOfDouble = new ArrayList<Double>();
		for (double value : arrayOfDouble){
			listOfDouble.add(new Double(value));
		}

		return listOfDouble;
	}
}
