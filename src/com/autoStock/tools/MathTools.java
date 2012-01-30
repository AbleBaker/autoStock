/**
 * 
 */
package com.autoStock.tools;

/**
 * @author Kevin Kowalewski
 *
 */
public class MathTools {
	public static String roundToTwoDecimalPlaces(double value){
		return String.valueOf(Math.round(value*100.0)/100.0);
	}
}
