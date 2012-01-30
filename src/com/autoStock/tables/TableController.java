/**
 * 
 */
package com.autoStock.tables;

import java.util.ArrayList;

import com.bethecoder.ascii_table.ASCIITable;

/**
 * @author Kevin Kowalewski
 *
 */
public class TableController {
	public void displayTable(TableDefinitions.Tables table, ArrayList<ArrayList<String>> values){
		
//		String[][] testString = new String[1][3];
//		testString[0][0] = "A";
//		testString[0][1] = "B";
//		testString[0][2] = "C";
//		
//		String[] columns = new String[3];
//		columns[0] = "C1";
//		columns[1] = "C2";
//		columns[2] = "C3";
//		
//		ASCIITable.getInstance().printTable(columns, testString);
	    ASCIITable.getInstance().printTable(new TableHelper().getArrayOfColumns(table.arrayOfColumns),new TableHelper().getArrayOfRows(table.arrayOfColumns,values));
	}
}
