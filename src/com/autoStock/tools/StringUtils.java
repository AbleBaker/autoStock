/**
 * 
 */
package com.autoStock.tools;

/**
 * @author Kevin Kowalewski
 *
 */
public class StringUtils {
	public static String removePrefix(String string, String prefix){
		return string.substring(string.indexOf(prefix)+1);
	}
}
