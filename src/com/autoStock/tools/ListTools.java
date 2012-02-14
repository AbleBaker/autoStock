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
}
