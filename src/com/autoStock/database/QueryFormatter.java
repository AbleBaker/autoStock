/**
 * 
 */
package com.autoStock.database;

import com.autoStock.Co;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;

/**
 * @author Kevin Kowalewski
 *
 */
public class QueryFormatter {
	public String format(BasicQueries basicQuery, QueryArgs... queryArgs){
		if (basicQuery.listOfFormatterArguments.length != queryArgs.length){
			throw new IllegalArgumentException("Invalid length of Formatter Arguments");
		}
		
		String[] listOfArguments = new String[queryArgs.length];
		
		int i = 0;
		for (QueryArgs argument : queryArgs){
			listOfArguments[i] = argument.value;
			i++;
		}
		
		return String.format(basicQuery.query, listOfArguments);
	}
}
