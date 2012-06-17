/**
 * 
 */
package com.autoStock.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.memoryCache.HashCache;
import com.autoStock.tools.MiscUtils;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseQuery {	
	
	private static HashCache hashCache = new HashCache();
	
	public Object getQueryResults(BasicQueries dbQuery, QueryArgs... queryArgs){
		try {
			String query = new QueryFormatter().format(dbQuery, queryArgs);
			String queryHash = MiscUtils.getHash(query);
			
			if (hashCache.containsKey(queryHash)){
				return hashCache.getValue(queryHash);
			}
			
			Connection connection = DatabaseCore.getConnection();
			Statement statement = connection.createStatement();
			//Co.println("Executing query: " + query);
			ResultSet resultSet = statement.executeQuery(query);
			
			ArrayList<Object> listOfResults = new ArrayList<Object>();
			
			while (resultSet.next()){
				listOfResults.add(new ResultInterpriter().interprit(resultSet, dbQuery.resultClass));
			}
			
			resultSet.close();
			statement.close();
			connection.close();
			
			hashCache.addValue(queryHash, listOfResults);
			
			return listOfResults;
			
		}catch (Exception e){
			Co.println("Could not execute query: " + new QueryFormatter().format(dbQuery, queryArgs));
			e.printStackTrace(); return null;}
	}
}
