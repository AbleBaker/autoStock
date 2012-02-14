/**
 * 
 */
package com.autoStock.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseQuery {
	public Object getQueryResults(BasicQueries dbQuery, QueryArgs... queryArgs){
		try {
			Connection connection = DatabaseCore.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(new QueryFormatter().format(dbQuery, queryArgs));
			
			ArrayList<Object> listOfResults = new ArrayList<Object>();
			
			while (resultSet.next()){
				listOfResults.add(new ResultInterpriter().interprit(resultSet, dbQuery.resultClass));
			}
			
			resultSet.close();
			statement.close();
			connection.close();
			
			return listOfResults;
			
		}catch (Exception e){e.printStackTrace(); return null;}
	}
}
