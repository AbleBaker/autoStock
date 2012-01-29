/**
 * 
 */
package com.autoStock.tables;

import java.util.ArrayList;

/**
 * @author Kevin Kowalewski
 *
 */
public class TableHelper {
	public String[][] getArrayOfRows(TableDefinitions.Columns[] arrayOfColumns, ArrayList<ArrayList<String>> values){
		String[][] arrayOfString = new String[values.size()][arrayOfColumns.length];
		
		int i=0;
		for (ArrayList<String> arrayListOfRows : values){
			int c=0;
			for (String value : arrayListOfRows){
				arrayOfString[i][c] = value;
				c++;
			}
			i++;
		}
		
		return arrayOfString;
	}
	
	public String[] getArrayOfColumns(TableDefinitions.Columns[] arrayOfColumns){
		String[] arrayOfString = new String[arrayOfColumns.length];
		
		int i=0;
		for (TableDefinitions.Columns column : arrayOfColumns){
			arrayOfString[i] = column.name().substring(0,1).toUpperCase() + column.name().substring(1);
		}
		
		return arrayOfString;
	}
}
