/**
 * 
 */
package com.autoStock.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;

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
	
	public static String getCommifiedValue(int number){
		NumberFormat numberFormat = NumberFormat.getInstance();
		return numberFormat.format(number);
	}
	
	public static String getCommifiedValue(double number){
		NumberFormat numberFormat = NumberFormat.getInstance();
		return numberFormat.format(number);
	}
	
	public static String getHash(String string){
		 try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			return new String(messageDigest.digest(string.getBytes()));
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
