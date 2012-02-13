/**
 * 
 */
package com.autoStock.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.autoStock.generated.basicDefinitions.BasicTableDefinitions;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;

/**
 * @author Kevin Kowalewski
 *
 */
public class DatabaseQuery {
	public ArrayList<Object> getQueryResults(){
		try {
			Connection connection = DatabaseCore.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from stockHistoricalPrices limit 10;");
			
			ArrayList<Object> listOfDbStockHistoricalPrice = new ArrayList<Object>();
			
			while (resultSet.next()){
//				DbStockHistoricalPrice dbStockHistoricalPrice = new BasicTableDefinitions(). new DbStockHistoricalPrice();
//				dbStockHistoricalPrice.getClass().getFields()[0].set(dbStockHistoricalPrice.getClass().getFields()[0].getType(), resultSet.getString(0));
				
			}
			
			return listOfDbStockHistoricalPrice;
			
		}catch (Exception e){e.printStackTrace(); return null;}
	}
}
