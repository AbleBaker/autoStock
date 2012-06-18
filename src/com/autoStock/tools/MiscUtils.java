/**
 * 
 */
package com.autoStock.tools;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;

import org.apache.commons.codec.digest.DigestUtils;

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
		return DigestUtils.md5Hex(string);
	}
}
