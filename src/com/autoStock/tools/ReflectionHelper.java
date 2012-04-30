/**
 * 
 */
package com.autoStock.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Kevin Kowalewski
 * 
 */
public class ReflectionHelper {
	public ArrayList<String> getValuesToStringArryay(Object object) {
		try {
			ArrayList<String> listOfString = new ArrayList<String>();
			for (Field field : object.getClass().getFields()) {
				if (field.getType() == String.class){
					listOfString.add((String) field.get(object));
				}else if (field.getType() == long.class || field.getType() == int.class || field.getType() == float.class){
					listOfString.add((String) String.valueOf(field.get(object)));
				}else if (field.getType() == Date.class){
					listOfString.add(DateTools.getPrettyDate((Date)field.get(object)));
				}else {
					throw new UnsatisfiedLinkError("Can't handle type");
				}
			}

			return listOfString;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}
}
