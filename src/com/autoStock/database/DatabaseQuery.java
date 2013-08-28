/**
 * 
 */
package com.autoStock.database;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.queryResults.QueryResult.QrExchange;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbExchange;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.memoryCache.DiskCache;
import com.autoStock.memoryCache.HashCache;
import com.autoStock.tools.MiscTools;
import com.google.gson.reflect.TypeToken;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseQuery {	
	private static HashCache hashCache = new HashCache();
	private static DiskCache diskCache = new DiskCache();
	
	public ArrayList<?> getQueryResults(BasicQueries dbQuery, QueryArg... queryArg){
		try {
			String query = new QueryFormatter().format(dbQuery, queryArg);
			String queryHash = MiscTools.getHash(query);
			
//			Co.println("Executing query: " + query);
			
			if (dbQuery.isCachable){
				if (hashCache.containsKey(queryHash)){
					return (ArrayList<?>) hashCache.getValue(queryHash);
				}
				
				if (diskCache.containsKey(queryHash) && getGsonType(dbQuery) != null){
	//				Co.println("--> Using disk cache");
					ArrayList<?> listOfResults = diskCache.getValue(queryHash, getGsonType(dbQuery));
					hashCache.addValue(queryHash, listOfResults);
					return listOfResults; 
				}
			}
			
			
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
			
			if (dbQuery.isCachable){
				hashCache.addValue(queryHash, listOfResults);
				diskCache.addValue(queryHash, listOfResults);
			}
			
			return listOfResults;
			
		}catch (Exception e){
			Co.println("Could not execute query: " + new QueryFormatter().format(dbQuery, queryArg));
			e.printStackTrace(); return null;}
	}
	
	public boolean insert(BasicQueries basicQuery, QueryArg... queryArg){
		try {
			String query = new QueryFormatter().format(basicQuery, queryArg);
		
			Connection connection = DatabaseCore.getConnection();
			Statement statement = connection.createStatement();
		
			statement.execute(query);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
//	public boolean insert(BasicQueries basicQuery, QueryArg... queryArg){
//		try {
//			String query = new QueryFormatter().format(basicQuery, queryArg);
//		
//			Connection connection = DatabaseCore.getConnection();
//			Statement statement = connection.createStatement();
//		
//			statement.execute(query);
//			return true;
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return false;
//	}

	private Type getGsonType(BasicQueries dbQuery) {
		if (dbQuery.resultClass == DbStockHistoricalPrice.class){
			return new TypeToken<ArrayList<DbStockHistoricalPrice>>(){}.getType();
		}else if (dbQuery.resultClass == DbExchange.class){
			return new TypeToken<ArrayList<DbExchange>>(){}.getType();
		}
		
		return null;
	}
}
