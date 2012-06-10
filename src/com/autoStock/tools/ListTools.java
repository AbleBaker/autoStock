/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Kevin Kowalewski
 *
 */
public class ListTools {
	public static void removeDuplicates(ArrayList<Object> arrayList){
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
}
