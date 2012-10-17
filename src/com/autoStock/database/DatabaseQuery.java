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
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.memoryCache.DiskCache;
import com.autoStock.memoryCache.HashCache;
import com.autoStock.tools.MiscTools;
import com.google.gson.Gson;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseQuery {	
	private static HashCache hashCache = new HashCache();
	private static DiskCache diskCache = new DiskCache();
	
	public ArrayList<?> getQueryResults(BasicQueries dbQuery, QueryArgs... queryArgs){
		try {
			String query = new QueryFormatter().format(dbQuery, queryArgs);
			String queryHash = MiscTools.getHash(query);
			
			if (hashCache.containsKey(queryHash)){
				return (ArrayList<?>) hashCache.getValue(queryHash);
			}
			
			if (diskCache.containsKey(queryHash) && dbQuery.resultClass == DbStockHistoricalPrice.class){
				return diskCache.getValue(queryHash, dbQuery.resultClass);
			}
			
//			Co.println("Executing query: " + query);
			
			Connection connection = DatabaseCore.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			ArrayList<Object> listOfResults = new ArrayList<Object>();

			while (resultSet.next()){
				listOfResults.add(new ResultInterpriter().interprit(resultSet, dbQuery.resultClass));
			}
			
			resultSet.close();
			statement.close();
			connection.close();
			
			hashCache.addValue(queryHash, listOfResults);
			if (dbQuery.resultClass == DbStockHistoricalPrice.class){
				diskCache.addValue(queryHash, listOfResults);
			}
			
			return listOfResults;
			
		}catch (Exception e){
			Co.println("Could not execute query: " + new QueryFormatter().format(dbQuery, queryArgs));
			e.printStackTrace(); return null;}
	}
}
