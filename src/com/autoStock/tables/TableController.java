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
	public void displayTable(TableDefinitions.AsciiTables table, ArrayList<ArrayList<String>> values){
		checkTable(table, values);
		
	    ASCIITable.getInstance().printTable(new TableHelper().getArrayOfColumns(table.arrayOfColumns), new TableHelper().getArrayOfRows(table.arrayOfColumns,values));
//		Co.println("\n" + getTable(table, values));
	}
	
	public void checkTable(TableDefinitions.AsciiTables table, ArrayList<ArrayList<String>> values){
		if (values.size() == 0){return;}
		
		if (table.arrayOfColumns == null || table.arrayOfColumns.length != values.get(0).size()){
			throw new IllegalStateException("AsciiTable definition, lenth of values don't match (columns, provided): " + table.arrayOfColumns.length + "," + values.get(0).size());
		}
	}
	
	public String getTable(TableDefinitions.AsciiTables table, ArrayList<ArrayList<String>> values){
		checkTable(table, values);
		
//		try {
			return ASCIITable.getInstance().getTable(new TableHelper().getArrayOfColumns(table.arrayOfColumns),new TableHelper().getArrayOfRows(table.arrayOfColumns,values));
//		}catch(IllegalArgumentException e){
//			Co.println("--> No table rows for: " + table.name());
//		}
		
//		return null;
	}
}
