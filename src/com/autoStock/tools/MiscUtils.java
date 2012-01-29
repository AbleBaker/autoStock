/**
 * 
 */
package com.autoStock.tools;

/**
 * @author Kevin Kowalewski
 *
 */
public class MiscUtils {
	public static int getArrayIndex(Object[] listOfObject, Object object){
		int i = 0;
		for (Object objectEntry : listOfObject){
			if (objectEntry == object){
				return i;
			}
			i++;
		}
		return -1;
	}
}
