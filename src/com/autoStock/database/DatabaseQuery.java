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

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseQuery {	
	public Object getQueryResults(BasicQueries dbQuery, QueryArgs... queryArgs){
		try {
			Connection connection = DatabaseCore.getConnection();
			Statement statement = connection.createStatement();
			String query = new QueryFormatter().format(dbQuery, queryArgs);
			//Co.println("Executing query: " + query);
			ResultSet resultSet = statement.executeQuery(query);
			
			ArrayList<Object> listOfResults = new ArrayList<Object>();
			
			while (resultSet.next()){
				listOfResults.add(new ResultInterpriter().interprit(resultSet, dbQuery.resultClass));
			}
			
			resultSet.close();
			statement.close();
			connection.close();
			
			return listOfResults;
			
		}catch (Exception e){
			Co.println("Could not execute query: " + new QueryFormatter().format(dbQuery, queryArgs));
			e.printStackTrace(); return null;}
	}
}
