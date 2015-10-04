/**
 * 
 */
package com.autoStock.tools;

import java.text.NumberFormat;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Kevin Kowalewski
 *
 */
public class MiscTools {
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
	
	public static String getCommifiedValue(int number){
		NumberFormat numberFormat = NumberFormat.getInstance();
		return numberFormat.format(number);
	}
	
	public static String getCommifiedValue(double number){
		if (number <= 0){return String.valueOf(number);}
		return getCommifiedValue(number, 2);
	}
	
	public static String getCommifiedValue(double number, int decimalPlaces){
		NumberFormat numberFormat = NumberFormat.getInstance();
		if (decimalPlaces != 0){
			numberFormat.setMinimumFractionDigits(decimalPlaces);
		}
		return numberFormat.format(number);
	}
	
	public static String getHash(String string){
		return DigestUtils.md5Hex(string);
	}
}
