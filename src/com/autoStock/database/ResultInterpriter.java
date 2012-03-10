/**
 * 
 */
package com.autoStock.database;

import java.sql.ResultSet;

import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultInterpriter {
	public Object interprit(ResultSet resultSet, Class resultClass){
		try {
			if (resultClass == DbStockHistoricalPrice.class){
				return new DatabaseBindinder().getDbStockHistoricalPrice(resultSet.getLong(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getDouble(6), resultSet.getInt(7), resultSet.getString(8));
			}
		}catch (Exception e){e.printStackTrace();}
		
		throw new UnsatisfiedLinkError();
	}
}
