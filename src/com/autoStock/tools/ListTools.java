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
}
